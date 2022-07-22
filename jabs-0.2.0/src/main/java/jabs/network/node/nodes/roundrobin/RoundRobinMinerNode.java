package jabs.network.node.nodes.roundrobin;

import jabs.consensus.algorithm.AbstractChainBasedConsensus;
import jabs.consensus.config.NakamotoConsensusConfig;
import jabs.ledgerdata.bitcoin.BitcoinBlockWithTx;
import jabs.ledgerdata.bitcoin.BitcoinBlockWithoutTx;
import jabs.ledgerdata.bitcoin.BitcoinCompactBlock;
import jabs.ledgerdata.bitcoin.BitcoinTx;
import jabs.network.message.DataMessage;
import jabs.network.message.Packet;
import jabs.network.networks.Network;
import jabs.network.node.nodes.MinerNode;
import jabs.simulator.Simulator;
import jabs.simulator.event.BlockMiningProcess;

import java.util.HashSet;
import java.util.Set;

public class RoundRobinMinerNode extends RoundRobinNode implements MinerNode {
    protected Set<BitcoinTx> memPool = new HashSet<>();
    protected final long hashPower;
    protected Simulator.ScheduledEvent miningProcess;

    static final long MAXIMUM_BLOCK_SIZE = 1800000;

    public RoundRobinMinerNode(Simulator simulator, Network network, int nodeID, long downloadBandwidth,
                            long uploadBandwidth, BitcoinBlockWithoutTx genesisBlock, long hashPower,
                            AbstractChainBasedConsensus<BitcoinBlockWithoutTx, BitcoinTx> consensusAlgorithm) {
        super(simulator, network, nodeID, downloadBandwidth, uploadBandwidth, consensusAlgorithm);
        this.hashPower = hashPower;
    }

    public RoundRobinMinerNode(Simulator simulator, Network network, int nodeID, long downloadBandwidth,
                            long uploadBandwidth, long hashPower, BitcoinBlockWithoutTx genesisBlock,
                            NakamotoConsensusConfig nakamotoConsensusConfig) {
        super(simulator, network, nodeID, downloadBandwidth, uploadBandwidth, genesisBlock, nakamotoConsensusConfig);
        this.hashPower = hashPower;
    }


    @Override
    public void generateNewBlock() {
        BitcoinBlockWithoutTx canonicalChainHead = this.consensusAlgorithm.getCanonicalChainHead();

        Set<BitcoinTx> blockTxs = new HashSet<>();
        long totalTxSize = 0;
        for (BitcoinTx bitcoinTx:memPool) {
            if ((totalTxSize + bitcoinTx.getSize()) > MAXIMUM_BLOCK_SIZE) {
                break;
            }
            blockTxs.add(bitcoinTx);
            totalTxSize += bitcoinTx.getSize();
        }

        BitcoinBlockWithTx roundRobinBlockWithTx = new BitcoinBlockWithTx(
                canonicalChainHead.getHeight()+1, simulator.getCurrentTime(),
                this.getConsensusAlgorithm().getCanonicalChainHead(), this, blockTxs,
                canonicalChainHead.getDifficulty()); // TODO: Difficulty adjustment?

        BitcoinCompactBlock bitcoinCompactBlock = new BitcoinCompactBlock(roundRobinBlockWithTx);

        this.processIncomingPacket(
                new Packet(
                        this, this, new DataMessage(bitcoinCompactBlock)
                )
        );
    }

    /**
     *
     */
    @Override
    public void startMining() {
        double currentDifficulty = this.consensusAlgorithm.getCanonicalChainHead().getDifficulty();
        BlockMiningProcess blockMiningProcess = new BlockMiningProcess(this.simulator, this.network.getRandom(),
                currentDifficulty/this.hashPower, this);
        this.miningProcess = this.simulator.putEvent(blockMiningProcess, blockMiningProcess.timeToNextGeneration());
    }

    /**
     *
     */
    @Override
    public void stopMining() {
        simulator.removeEvent(this.miningProcess);
    }

    @Override
    public long getHashPower() {
        return this.hashPower;
    }
    public int getMinerId() { return this.nodeID; }
}
