package jabs.network.networks.roundrobin;

import jabs.consensus.config.ChainBasedConsensusConfig;
import jabs.consensus.config.NakamotoConsensusConfig;
import jabs.ledgerdata.bitcoin.BitcoinBlockWithoutTx;
import jabs.network.networks.GlobalProofOfWorkNetwork;
import jabs.network.node.nodes.bitcoin.BitcoinMinerNode;
import jabs.network.node.nodes.bitcoin.BitcoinNode;
import jabs.network.stats.ProofOfWorkGlobalNetworkStats;
import jabs.simulator.Simulator;
import jabs.simulator.randengine.RandomnessEngine;

public class RoundRobinGlobalProofOfWorkNetwork<R extends Enum<R>> extends
        GlobalProofOfWorkNetwork<BitcoinNode, BitcoinMinerNode, BitcoinBlockWithoutTx, R> {

    public RoundRobinGlobalProofOfWorkNetwork(RandomnessEngine randomnessEngine,
                                           ProofOfWorkGlobalNetworkStats<R> networkStats) {
        super(randomnessEngine, networkStats);
    }

    /**
     * @param difficulty the difficulty value of the genesis block
     * @return the genesis block with no parents
     */
    @Override
    public BitcoinBlockWithoutTx genesisBlock(double difficulty) {
        return new BitcoinBlockWithoutTx(0, 0, 0, null, null, difficulty);
    }

    @Override
    public BitcoinNode createSampleNode(Simulator simulator, int nodeID, BitcoinBlockWithoutTx genesisBlock,
                                        ChainBasedConsensusConfig chainBasedConsensusConfig) {
        R region = (R) this.sampleRegion();
        return new BitcoinNode(simulator, this, nodeID, this.sampleDownloadBandwidth(region),
                this.sampleUploadBandwidth(region), genesisBlock, (NakamotoConsensusConfig) chainBasedConsensusConfig);
    }

    @Override
    public BitcoinMinerNode createSampleMiner(Simulator simulator, int nodeID,  long hashPower,
                                              BitcoinBlockWithoutTx genesisBlock,
                                              ChainBasedConsensusConfig chainBasedConsensusConfig) {
        R region = (R) this.sampleRegion();
        return new BitcoinMinerNode(simulator, this, nodeID, this.sampleDownloadBandwidth(region),
                this.sampleUploadBandwidth(region), hashPower, genesisBlock,
                (NakamotoConsensusConfig) chainBasedConsensusConfig);
    }
}
