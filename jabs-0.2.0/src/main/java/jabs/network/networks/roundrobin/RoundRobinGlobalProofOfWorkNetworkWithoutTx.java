package jabs.network.networks.roundrobin;

import jabs.consensus.config.ChainBasedConsensusConfig;
import jabs.ledgerdata.bitcoin.BitcoinBlockWithoutTx;
import jabs.network.networks.bitcoin.BitcoinGlobalProofOfWorkNetwork;
import jabs.network.node.nodes.bitcoin.BitcoinMinerNode;
import jabs.network.node.nodes.bitcoin.BitcoinMinerNodeWithoutTx;
import jabs.network.stats.ProofOfWorkGlobalNetworkStats;
import jabs.simulator.Simulator;
import jabs.simulator.randengine.RandomnessEngine;

public class RoundRobinGlobalProofOfWorkNetworkWithoutTx<R extends Enum<R>> extends BitcoinGlobalProofOfWorkNetwork<R> {
    public RoundRobinGlobalProofOfWorkNetworkWithoutTx(RandomnessEngine randomnessEngine,
                                                    ProofOfWorkGlobalNetworkStats<R> networkStats) {
        super(randomnessEngine, networkStats);
    }

    @Override
    public BitcoinMinerNode createSampleMiner(Simulator simulator, int nodeID, long hashPower, BitcoinBlockWithoutTx genesisBlock,
                                              ChainBasedConsensusConfig chainBasedConsensusConfig) {
        R region = this.sampleRegion();
        return new BitcoinMinerNodeWithoutTx(simulator, this, nodeID, this.sampleDownloadBandwidth(region),
                this.sampleUploadBandwidth(region), hashPower, genesisBlock, chainBasedConsensusConfig);
    }
}
