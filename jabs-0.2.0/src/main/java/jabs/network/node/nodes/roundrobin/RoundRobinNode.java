package jabs.network.node.nodes.roundrobin;

import jabs.consensus.algorithm.AbstractChainBasedConsensus;
import jabs.consensus.algorithm.NakamotoConsensus;
import jabs.consensus.blockchain.LocalBlockTree;
import jabs.consensus.config.NakamotoConsensusConfig;
import jabs.ledgerdata.TransactionFactory;
import jabs.ledgerdata.Vote;
import jabs.ledgerdata.bitcoin.BitcoinBlockWithoutTx;
import jabs.ledgerdata.bitcoin.BitcoinTx;
import jabs.network.message.InvMessage;
import jabs.network.message.Packet;
import jabs.network.networks.Network;
import jabs.network.node.nodes.Node;
import jabs.network.node.nodes.PeerBlockchainNode;
import jabs.network.p2p.BitcoinCoreP2P;
import jabs.simulator.Simulator;

public class RoundRobinNode extends PeerBlockchainNode<BitcoinBlockWithoutTx, BitcoinTx> {
    public RoundRobinNode(Simulator simulator, Network network, int nodeID, long downloadBandwidth, long uploadBandwidth,
                       BitcoinBlockWithoutTx genesisBlock, NakamotoConsensusConfig nakamotoConsensusConfig) {
        super(simulator, network, nodeID, downloadBandwidth, uploadBandwidth,
                new BitcoinCoreP2P(),
                new NakamotoConsensus<>(new LocalBlockTree<>(genesisBlock), nakamotoConsensusConfig));
    }

    public RoundRobinNode(Simulator simulator, Network network, int nodeID, long downloadBandwidth, long uploadBandwidth,
                       AbstractChainBasedConsensus<BitcoinBlockWithoutTx, BitcoinTx> consensusAlgorithm) {
        super(simulator, network, nodeID, downloadBandwidth, uploadBandwidth,
                new BitcoinCoreP2P(), consensusAlgorithm);
    }

    @Override
    protected void processNewTx(BitcoinTx bitcoinTx, Node from) {
        this.broadcastTxInvMessage(bitcoinTx);
    }

    @Override
    protected void processNewBlock(BitcoinBlockWithoutTx bitcoinBlock) {
        this.consensusAlgorithm.newIncomingBlock(bitcoinBlock);
        this.broadcastBlockInvMessage(bitcoinBlock);
    }

    @Override
    protected void processNewVote(Vote vote) {

    }

    protected void broadcastTxInvMessage(BitcoinTx tx) {
        for (Node neighbor:this.p2pConnections.getNeighbors()) {
            this.nodeNetworkInterface.addToUpLinkQueue(
                    new Packet(this, neighbor,
                            new InvMessage(tx.getHash().getSize(), tx.getHash())
                    )
            );
        }
    }

    protected void broadcastBlockInvMessage(BitcoinBlockWithoutTx block) {
        if(this.consensusAlgorithm.isBlockValid(block)) {
            for (Node neighbor:this.p2pConnections.getNeighbors()) {
                this.nodeNetworkInterface.addToUpLinkQueue(
                        new Packet(this, neighbor,
                                new InvMessage(block.getHash().getSize(), block.getHash())
                        )
                );
            }
        }
    }

    @Override
    public void generateNewTransaction() {
        BitcoinTx tx = TransactionFactory.sampleBitcoinTransaction(network.getRandom());
        this.alreadySeenTxs.put(tx.getHash(), tx);
        broadcastTxInvMessage(tx);
    }
}
