package org.apache.zookeeper.server;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.zookeeper.server.quorum.QuorumPeerConfig;
import org.apache.zookeeper.server.quorum.QuorumPeerConfig.ConfigException;
import org.apache.zookeeper.server.quorum.QuorumPeerMain;
/**
 * 该类监听后不会阻塞
 * @author gjs
 *
 */
public class ZkQuorumMain extends QuorumPeerMain {
	private static final Logger LOG = Logger.getLogger(ZkQuorumMain.class);
	private ZkServerMain zkServer;
	
	@Override
	protected void initializeAndRun(String[] args) throws ConfigException, IOException {
        QuorumPeerConfig config = new QuorumPeerConfig();
        if (args.length == 1) {
            config.parse(args[0]);
        }

        // Start and schedule the the purge task
        DatadirCleanupManager purgeMgr = new DatadirCleanupManager(config
                .getDataDir(), config.getDataLogDir(), config
                .getSnapRetainCount(), config.getPurgeInterval());
        purgeMgr.start();

        if (args.length == 1 && config.getServers().size() > 0) {
            runFromConfig(config);
        } else {
            LOG.warn("Either no config or no quorum defined in config, running "
                    + " in standalone mode");
            // there is only server in the quorum -- run as standalone
            zkServer = ZkServerMain.start(args);
        }
    }

	public static ZkQuorumMain start(String[] args) {
		ZkQuorumMain main = new ZkQuorumMain();
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
	
	public void shutdown(){
		zkServer.shutdown();
	}
}
