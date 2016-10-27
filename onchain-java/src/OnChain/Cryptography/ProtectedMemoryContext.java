package OnChain.Cryptography;

class ProtectedMemoryContext implements AutoCloseable
{
//TODO
//    private static Dictionary<byte[], byte> counts = new Dictionary<byte[], byte>();
//    private byte[] memory;
//    private MemoryProtectionScope scope;
//
//    public ProtectedMemoryContext(byte[] memory, MemoryProtectionScope scope)
//    {
//        this.memory = memory;
//        this.scope = scope;
//        if (counts.ContainsKey(memory))
//        {
//            counts[memory]++;
//        }
//        else
//        {
//            counts.Add(memory, 1);
//            ProtectedMemory.Unprotect(memory, scope);
//        }
//    }
	
	@Override
    public void close()
    {
		//TODO
//        if (--counts[memory] == 0)
//        {
//            counts.Remove(memory);
//            ProtectedMemory.Protect(memory, scope);
//        }
    }
}
