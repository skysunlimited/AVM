package org.aion.avm.core.blockchainruntime;

import org.aion.kernel.TestingState;
import org.aion.types.AionAddress;
import org.aion.types.Transaction;
import org.aion.avm.core.IExternalCapabilities;
import i.RuntimeAssertionError;


/**
 * Most of our tests don't need any capabilities so this is provided to fail on unexpected calls.
 */
public class EmptyCapabilities implements IExternalCapabilities {
    @Override
    public byte[] sha256(byte[] data) {
        throw RuntimeAssertionError.unimplemented("Not called in test");
    }

    @Override
    public byte[] blake2b(byte[] data) {
        throw RuntimeAssertionError.unimplemented("Not called in test");
    }

    @Override
    public byte[] keccak256(byte[] data) {
        byte[] hash = new byte[32];
        int u = Math.min(32, data.length);
        System.arraycopy(data, 0, hash, 32 - u, u);
        return hash;
    }

    @Override
    public boolean verifyEdDSA(byte[] data, byte[] signature, byte[] publicKey) {
        throw RuntimeAssertionError.unimplemented("Not called in test");
    }

    @Override
    public AionAddress generateContractAddress(Transaction tx) {
        // NOTE:  This address generation isn't anything particular.  It is just meant to be deterministic and derived from the tx.
        // It is NOT meant to be equivalent/similar to the implementation used by an actual kernel.
        byte[] senderAddressBytes = tx.senderAddress.toByteArray();
        byte[] raw = new byte[AionAddress.LENGTH];
        for (int i = 0; i < AionAddress.LENGTH - 1; ++i) {
            byte one = (i < senderAddressBytes.length) ? senderAddressBytes[i] : (byte)i;
            byte two = (i < tx.nonce.toByteArray().length) ? tx.nonce.toByteArray()[i] : (byte)i;
            // We write into the (i+1)th byte because the 0th byte is for the prefix.
            // This means we will have a collision if an address reaches nonces that agree on the first 31 bytes,
            // but that number is huge, so it's fine for testing purposes.
            raw[i+1] = (byte) (one + two);
        }
        raw[0] = TestingState.AVM_CONTRACT_PREFIX;
        return new AionAddress(raw);
    }
}
