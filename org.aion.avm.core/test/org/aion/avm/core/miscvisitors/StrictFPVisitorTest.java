package org.aion.avm.core.miscvisitors;

import java.math.BigInteger;

import org.aion.avm.core.AvmTransactionUtil;
import org.aion.kernel.AvmTransactionResult;
import org.aion.kernel.TestingState;
import org.aion.types.AionAddress;
import org.aion.types.Transaction;
import org.aion.avm.core.AvmConfiguration;
import org.aion.avm.core.AvmImpl;
import org.aion.avm.core.CommonAvmFactory;
import org.aion.avm.core.blockchainruntime.EmptyCapabilities;
import org.aion.avm.core.dappreading.JarBuilder;
import org.aion.avm.core.dappreading.LoadedJar;
import org.aion.avm.core.util.Helpers;
import org.aion.avm.userlib.CodeAndArguments;
import org.aion.kernel.TestingBlock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;

import static org.junit.Assert.assertTrue;
import static org.objectweb.asm.Opcodes.ACC_STRICT;


public class StrictFPVisitorTest {
    // transaction
    private long energyLimit = 10_000_000L;
    private long energyPrice = 1L;

    private AionAddress deployer = TestingState.PREMINED_ADDRESS;
    private AionAddress dappAddress;

    private TestingState kernel;
    private AvmImpl avm;

    @Before
    public void setup() {
        TestingBlock block = new TestingBlock(new byte[32], 1, Helpers.randomAddress(), System.currentTimeMillis(), new byte[0]);
        this.kernel = new TestingState(block);
        this.avm = CommonAvmFactory.buildAvmInstanceForConfiguration(new EmptyCapabilities(), new AvmConfiguration());
        
        byte[] jar = JarBuilder.buildJarForMainAndClasses(StrictFPVisitorTestResource.class);
        byte[] arguments = null;
        Transaction tx = AvmTransactionUtil.create(deployer, kernel.getNonce(deployer), BigInteger.ZERO, new CodeAndArguments(jar, arguments).encodeToBytes(), energyLimit, energyPrice);
        AvmTransactionResult txResult = avm.run(this.kernel, new Transaction[] {tx})[0].get();

        dappAddress = new AionAddress(txResult.getReturnData());
        assertTrue(null != dappAddress);
    }

    @After
    public void tearDown() {
        this.avm.shutdown();
    }

    @Test
    public void testAccessFlag() {
        LoadedJar jar = LoadedJar.fromBytes(kernel.getTransformedCode(dappAddress));
        for (byte[] klass : jar.classBytesByQualifiedNames.values()) {
            ClassReader reader = new ClassReader(klass);
            ClassNode node = new ClassNode();
            reader.accept(node, ClassReader.SKIP_FRAMES);
            assertTrue((node.access & ACC_STRICT) != 0);
        }
    }

    @Test
    public void testFp() {
        Transaction tx = AvmTransactionUtil.call(deployer, dappAddress, kernel.getNonce(deployer), BigInteger.ZERO, new byte[0], energyLimit, energyPrice);
        AvmTransactionResult txResult = avm.run(this.kernel, new Transaction[] {tx})[0].get();
        assertTrue(txResult.getResultCode().isSuccess());
    }
}
