package org.apache.zookeeper.server;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import javax.management.JMException;

import org.apache.yetus.audience.InterfaceAudience;
import org.apache.zookeeper.jmx.ManagedUtil;
import org.apache.zookeeper.server.ServerCnxnFactory;
import org.apache.zookeeper.server.ServerConfig;
import org.apache.zookeeper.server.ZooKeeperServer;
import org.apache.zookeeper.server.ZooKeeperServerShutdownHandler;
import org.apache.zookeeper.server.persistence.FileTxnSnapLog;
import org.apache.zookeeper.server.quorum.QuorumPeerConfig.ConfigException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 监听后不阻塞,重写ZooKeeperServerMain
 * @author gjs
 *
 */
@InterfaceAudience.Public
public class ZkServerMain {
    private static final Logger LOG =
            LoggerFactory.getLogger(ZkServerMain.class);

        private ServerCnxnFactory cnxnFactory;
        private ZooKeeperServer zkServer;
        private FileTxnSnapLog txnLog;

        protected void initializeAndRun(String[] args)
            throws ConfigException, IOException
        {
            try {
                ManagedUtil.registerLog4jMBeans();
            } catch (JMException e) {
                LOG.warn("Unable to register log4j JMX control", e);
            }

            ServerConfig config = new ServerConfig();
            if (args.length == 1) {
                config.parse(args[0]);
            } else {
                config.parse(args);
            }

            runFromConfig(config);
        }

        /**
         * Run from a ServerConfig.
         * @param config ServerConfig to use.
         * @throws IOException
         */
        public void runFromConfig(ServerConfig config) throws IOException {
            LOG.info("Starting server");
            try {
                // Note that this thread isn't going to be doing anything else,
                // so rather than spawning another thread, we will just call
                // run() in this thread.
                // create a file logger url from the command line args
                final ZooKeeperServer zkServer = new ZooKeeperServer();
                // Registers shutdown handler which will be used to know the
                // server error or shutdown state changes.
                final CountDownLatch shutdownLatch = new CountDownLatch(1);
                zkServer.registerServerShutdownHandler(
                        new ZooKeeperServerShutdownHandler(shutdownLatch));

                txnLog = new FileTxnSnapLog(new File(config.getDataLogDir()), new File(
                        config.getDataDir()));
                zkServer.setTxnLogFactory(txnLog);
                zkServer.setTickTime(config.getTickTime());
                zkServer.setMinSessionTimeout(config.getMinSessionTimeout());
                zkServer.setMaxSessionTimeout(config.getMaxSessionTimeout());
                cnxnFactory = ServerCnxnFactory.createFactory();
                cnxnFactory.configure(config.getClientPortAddress(),
                        config.getMaxClientCnxns());
                cnxnFactory.startup(zkServer);
                this.zkServer = zkServer;
                // Watch status of ZooKeeper server. It will do a graceful shutdown
                // if the server is not running or hits an internal error.
            } catch (InterruptedException e) {
                // warn, but generally this is ok
                LOG.warn("Server interrupted", e);
            } finally {
                
            }
        }

        /**
         * Shutdown the serving instance
         */
        public void shutdown() {
            if (cnxnFactory != null) {
                cnxnFactory.shutdown();
            }
            try {
				cnxnFactory.join();
				if (zkServer.canShutdown()) {
					zkServer.shutdown(true);
				}
			} catch (InterruptedException e) {
				LOG.error("cnxnFactory close fail!", e);
			}finally{
				if (txnLog != null) {
                    try {
						txnLog.close();
					} catch (IOException e) {
						LOG.error("txnLog close fail!", e);
					}
                }
			}
        }

        // VisibleForTesting
        ServerCnxnFactory getCnxnFactory() {
            return cnxnFactory;
        }

		public static ZkServerMain start(String[] args) {
        	ZkServerMain main = new ZkServerMain();
            try {
                main.initializeAndRun(args);
            } catch (IllegalArgumentException e) {
                LOG.error("Invalid arguments, exiting abnormally", e);
            } catch (ConfigException e) {
                LOG.error("Invalid config, exiting abnormally", e);
            } catch (Exception e) {
                LOG.error("Unexpected exception, exiting abnormally", e);
            }
            return main;
        }
}
