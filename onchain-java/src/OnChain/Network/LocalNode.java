package OnChain.Network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;

import OnChain.UInt256;
import OnChain.Core.Block;
import OnChain.Core.Transaction;
import OnChain.Core.TransactionInput;
import OnChain.IO.Caching.RelayCache;

public class LocalNode // TODO : IDisposable
{
    // TODO
    //public static event EventHandler<Inventory> NewInventory;

    public final int PROTOCOL_VERSION = 0;
    private final int CONNECTED_MAX = 10;
    private final int PENDING_MAX = CONNECTED_MAX;
    private final int UNCONNECTED_MAX = 1000;
//#if TESTNET
    public final int DEFAULT_PORT = 20333;
//#else
//    public final int DEFAULT_PORT = 10333;
//#endif

    //TODO: 需要搭建一批种子节点
    private static final String[] SeedList =
    {
        "seed1.antshares.org",
        "seed2.antshares.org",
        "seed3.antshares.org",
        "seed4.antshares.org",
        "seed5.antshares.org"
    };

    private static final Map<UInt256, Transaction> MemoryPool = new HashMap<UInt256, Transaction>();
    static final HashSet<UInt256> KnownHashes = new HashSet<UInt256>();

    final RelayCache relayCache = new RelayCache(100);

    private static final HashSet<IPEndPoint> unconnectedPeers = new HashSet<IPEndPoint>();
    private static final HashSet<IPEndPoint> badPeers = new HashSet<IPEndPoint>();
    final Map<IPEndPoint, RemoteNode> pendingPeers = new HashMap<IPEndPoint, RemoteNode>();
    final List<RemoteNode> connectedPeers = new ArrayList<RemoteNode>();

    static final HashSet<IPAddress> LocalAddresses = new HashSet<IPAddress>();
    short Port;
    final int Nonce;
    //private TcpListener listener;
    private Thread connectThread;
    private Thread listenerThread;
    private int started = 0;
    private int disposed = 0;

    private boolean GlobalMissionsEnabled = true;// { get; set; } = true;
    public boolean isGlobalMissionsEnabled() {
        return GlobalMissionsEnabled;
    }

    public void setGlobalMissionsEnabled(boolean globalMissionsEnabled) {
        GlobalMissionsEnabled = globalMissionsEnabled;
    }
    
    
    public int getRemoteNodeCount() { return connectedPeers.size(); }

    private boolean ServiceEnabled = true;// { get; set; } = true;
    private boolean UpnpEnabled = false;// { get; set; } = false;
    private String UserAgent;// { get; set; }
    public boolean isServiceEnabled() {
        return ServiceEnabled;
    }

    public void setServiceEnabled(boolean serviceEnabled) {
        ServiceEnabled = serviceEnabled;
    }

    public boolean isUpnpEnabled() {
        return UpnpEnabled;
    }

    public void setUpnpEnabled(boolean upnpEnabled) {
        UpnpEnabled = upnpEnabled;
    }

    public String getUserAgent() {
        return UserAgent;
    }

    public void setUserAgent(String userAgent) {
        UserAgent = userAgent;
    }
    
//TODO
//    static LocalNode()
//    {
//        try
//        {
//            LocalAddresses.UnionWith(Dns.GetHostEntry(Dns.GetHostName()).AddressList.Where(p => !IPAddress.IsLoopback(p)).Select(p => p.MapToIPv6()));
//        }
//        catch (SocketException) { }
//        Blockchain.PersistCompleted += Blockchain_PersistCompleted;
//    }
//
    public LocalNode()
    {
        Random rand = new Random();
        this.Nonce = (int)rand.nextInt();
        this.connectThread = new Thread(()->ConnectToPeersLoop());
        this.connectThread.setDaemon(true);
        this.connectThread.setName("LocalNode.ConnectToPeersLoop");
        this.listenerThread = new Thread(()->AcceptPeersLoop());
        this.listenerThread.setDaemon(true);
        this.listenerThread.setName("LocalNode.AcceptPeersLoop");
        //this.UserAgent = String.format("/AntSharesCore:{0}/", Version.ToString());
    }

    private void AcceptPeersLoop()
    {
        while (disposed == 0)
        {
//            TcpClient tcp;
//            try
//            {
//                tcp = listener.AcceptTcpClient();
//            }
//            catch (ObjectDisposedException)
//            {
//                break;
//            }
//            catch (SocketException)
//            {
//                break;
//            }
//            RemoteNode remoteNode = new RemoteNode(this, tcp);
//            remoteNode.Disconnected += RemoteNode_Disconnected;
//            remoteNode.InventoryReceived += RemoteNode_InventoryReceived;
//            remoteNode.PeersReceived += RemoteNode_PeersReceived;
//            remoteNode.StartProtocol();
        }
    }

    private static boolean AddTransaction(Transaction tx)
    {
        return false;
        // TODO
//        if (Blockchain.Default == null) return false;
//        synchronized (MemoryPool)
//        {
//            if (MemoryPool.ContainsKey(tx.Hash)) return false;
//            if (MemoryPool.Values.SelectMany(p => p.GetAllInputs()).Intersect(tx.GetAllInputs()).Count() > 0)
//                return false;
//            if (Blockchain.Default.ContainsTransaction(tx.Hash)) return false;
//            if (!tx.Verify()) return false;
//            MemoryPool.Add(tx.Hash, tx);
//            return true;
//        }
    }

    public static void AllowHashes(Iterable<UInt256> hashes)
    {
        synchronized (KnownHashes)
        {
            // TODO
            //KnownHashes.ExceptWith(hashes);
        }
    }

    private static void Blockchain_PersistCompleted(Object sender, Block block)
    {
//        HashSet<TransactionInput> inputs = new HashSet<TransactionInput>(block.Transactions.SelectMany(p => p.GetAllInputs()));
//        synchronized (MemoryPool)
//        {
//            foreach (Transaction tx in block.Transactions)
//            {
//                MemoryPool.Remove(tx.Hash);
//            }
//            foreach (Transaction tx in MemoryPool.Values.ToArray())
//            {
//                foreach (TransactionInput input in tx.GetAllInputs())
//                    if (inputs.Contains(input))
//                    {
//                        MemoryPool.Remove(tx.Hash);
//                        break;
//                    }
//            }
//        }
    }

    // TODO
    public /*async Task*/ void ConnectToPeerAsync(String hostNameOrAddress)
    {
//        IPAddress ipAddress;
//        if (IPAddress.TryParse(hostNameOrAddress, out ipAddress))
//        {
//            ipAddress = ipAddress.MapToIPv6();
//        }
//        else
//        {
//            IPHostEntry entry;
//            try
//            {
//                entry = await Dns.GetHostEntryAsync(hostNameOrAddress);
//            }
//            catch (SocketException)
//            {
//                return;
//            }
//            ipAddress = entry.AddressList.FirstOrDefault(p => p.AddressFamily == AddressFamily.InterNetwork || p.IsIPv6Teredo)?.MapToIPv6();
//            if (ipAddress == null) return;
//        }
//        await ConnectToPeerAsync(new IPEndPoint(ipAddress, DEFAULT_PORT));
    }

    // TODO
    public /*async Task*/ void ConnectToPeerAsync(IPEndPoint remoteEndpoint)
    {
//        if (remoteEndpoint.Port == Port && LocalAddresses.Contains(remoteEndpoint.Address)) return;
//        RemoteNode remoteNode;
//        synchronized (unconnectedPeers)
//        {
//            unconnectedPeers.Remove(remoteEndpoint);
//        }
//        synchronized (pendingPeers)
//        {
//            synchronized (connectedPeers)
//            {
//                if (pendingPeers.ContainsKey(remoteEndpoint) || connectedPeers.Any(p => remoteEndpoint.Equals(p.ListenerEndpoint)))
//                    return;
//            }
//            remoteNode = new RemoteNode(this, remoteEndpoint);
//            pendingPeers.Add(remoteEndpoint, remoteNode);
//            remoteNode.Disconnected += RemoteNode_Disconnected;
//            remoteNode.InventoryReceived += RemoteNode_InventoryReceived;
//            remoteNode.PeersReceived += RemoteNode_PeersReceived;
//        }
//        await remoteNode.ConnectAsync();
    }

    private void ConnectToPeersLoop()
    {
//        while (disposed == 0)
//        {
//            int connectedCount = connectedPeers.Count;
//            int pendingCount = pendingPeers.Count;
//            int unconnectedCount = unconnectedPeers.Count;
//            int maxConnections = Math.Max(CONNECTED_MAX + CONNECTED_MAX / 5, PENDING_MAX);
//            if (connectedCount < CONNECTED_MAX && pendingCount < PENDING_MAX && (connectedCount + pendingCount) < maxConnections)
//            {
//                Task[] tasks = { };
//                if (unconnectedCount > 0)
//                {
//                    IPEndPoint[] endpoints;
//                    synchronized (unconnectedPeers)
//                    {
//                        endpoints = unconnectedPeers.Take(maxConnections - (connectedCount + pendingCount)).ToArray();
//                    }
//                    tasks = endpoints.Select(p => ConnectToPeerAsync(p)).ToArray();
//                }
//                else if (connectedCount > 0)
//                {
//                    synchronized (connectedPeers)
//                    {
//                        foreach (RemoteNode node in connectedPeers)
//                            node.RequestPeers();
//                    }
//                }
//                else
//                {
//                    tasks = SeedList.Select(p => ConnectToPeerAsync(p)).ToArray();
//                }
//                Task.WaitAll(tasks);
//            }
//            for (int i = 0; i < 50 && disposed == 0; i++)
//            {
//                Thread.Sleep(100);
//            }
//        }
    }

    public static boolean ContainsTransaction(UInt256 hash)
    {
//        synchronized (MemoryPool)
//        {
//            return MemoryPool.ContainsKey(hash);
//        }
        return false;
    }

    public void Dispose()
    {
//        if (Interlocked.Exchange(ref disposed, 1) == 0)
//        {
//            if (started > 0)
//            {
//                if (listener != null) listener.Stop();
//                if (!connectThread.ThreadState.HasFlag(ThreadState.Unstarted)) connectThread.Join();
//                if (!listenerThread.ThreadState.HasFlag(ThreadState.Unstarted)) listenerThread.Join();
//                synchronized (unconnectedPeers)
//                {
//                    if (unconnectedPeers.Count < UNCONNECTED_MAX)
//                    {
//                        synchronized (connectedPeers)
//                        {
//                            unconnectedPeers.UnionWith(connectedPeers.Select(p => p.ListenerEndpoint).Where(p => p != null).Take(UNCONNECTED_MAX - unconnectedPeers.Count));
//                        }
//                    }
//                }
//                RemoteNode[] nodes;
//                synchronized (connectedPeers)
//                {
//                    nodes = connectedPeers.ToArray();
//                }
//                Task.WaitAll(nodes.Select(p => Task.Run(() => p.Disconnect(false))).ToArray());
//            }
//        }
    }

    public static Iterable<Transaction> GetMemoryPool()
    {
//        synchronized (MemoryPool)
//        {
//            foreach (Transaction tx in MemoryPool.Values)
//                yield return tx;
//        }
        return null;
    }

    public RemoteNode[] GetRemoteNodes()
    {
        return null;
//        synchronized (connectedPeers)
//        {
//            return connectedPeers.ToArray();
//        }
    }

    private static boolean IsIntranetAddress(IPAddress address)
    {
        return false;
//        byte[] data = address.GetAddressBytes();
//        Array.Reverse(data);
//        int value = BitConverter.ToUInt32(data, 0);
//        return (value & 0xff000000) == 0x0a000000 || (value & 0xfff00000) == 0xac100000 || (value & 0xffff0000) == 0xc0a80000;
    }

    // TODO Stream?
    public static void LoadState(/*Stream*/ Object stream)
    {
//        unconnectedPeers.Clear();
//        using (BinaryReader reader = new BinaryReader(stream, Encoding.ASCII, true))
//        {
//            int count = reader.ReadInt32();
//            for (int i = 0; i < count; i++)
//            {
//                IPAddress address = new IPAddress(reader.ReadBytes(4));
//                int port = reader.ReadUInt16();
//                unconnectedPeers.Add(new IPEndPoint(address.MapToIPv6(), port));
//            }
//        }
    }

    public boolean Relay(Inventory inventory)
    {
        return false;
//        if (connectedPeers.Count == 0) return false;
//        synchronized (KnownHashes)
//        {
//            if (!KnownHashes.Add(inventory.Hash)) return false;
//        }
//        if (inventory is Block)
//        {
//            if (Blockchain.Default == null) return false;
//            Block block = (Block)inventory;
//            if (Blockchain.Default.ContainsBlock(block.Hash)) return false;
//            if (!Blockchain.Default.AddBlock(block)) return false;
//        }
//        else if (inventory is Transaction)
//        {
//            if (!AddTransaction((Transaction)inventory)) return false;
//        }
//        else //if (inventory is Consensus)
//        {
//            if (!inventory.Verify()) return false;
//        }
//        synchronized (connectedPeers)
//        {
//            if (connectedPeers.Count == 0) return false;
//            RelayCache.Add(inventory);
//            foreach (RemoteNode node in connectedPeers)
//                node.Relay(inventory);
//        }
//        NewInventory?.Invoke(this, inventory);
//        return true;
    }

    private void RemoteNode_Disconnected(Object sender, boolean error)
    {
//        RemoteNode remoteNode = (RemoteNode)sender;
//        remoteNode.Disconnected -= RemoteNode_Disconnected;
//        remoteNode.InventoryReceived -= RemoteNode_InventoryReceived;
//        remoteNode.PeersReceived -= RemoteNode_PeersReceived;
//        if (error && remoteNode.ListenerEndpoint != null)
//        {
//            synchronized (badPeers)
//            {
//                badPeers.Add(remoteNode.ListenerEndpoint);
//            }
//        }
//        synchronized (unconnectedPeers)
//        {
//            synchronized (pendingPeers)
//            {
//                synchronized (connectedPeers)
//                {
//                    if (remoteNode.ListenerEndpoint != null)
//                    {
//                        unconnectedPeers.Remove(remoteNode.ListenerEndpoint);
//                        pendingPeers.Remove(remoteNode.ListenerEndpoint);
//                    }
//                    connectedPeers.Remove(remoteNode);
//                }
//            }
//        }
    }

    private void RemoteNode_InventoryReceived(Object sender, Inventory inventory)
    {
        Relay(inventory);
    }

    private void RemoteNode_PeersReceived(Object sender, IPEndPoint[] peers)
    {
//        synchronized (unconnectedPeers)
//        {
//            if (unconnectedPeers.Count < UNCONNECTED_MAX)
//            {
//                synchronized (badPeers)
//                {
//                    synchronized (pendingPeers)
//                    {
//                        synchronized (connectedPeers)
//                        {
//                            unconnectedPeers.UnionWith(peers);
//                            unconnectedPeers.ExceptWith(badPeers);
//                            unconnectedPeers.ExceptWith(pendingPeers.Keys);
//                            unconnectedPeers.ExceptWith(connectedPeers.Select(p => p.ListenerEndpoint));
//                        }
//                    }
//                }
//            }
//        }
    }

    // TODO Stream
    public static void SaveState(/*Stream*/ Object stream)
    {
        IPEndPoint[] peers;
//        synchronized (unconnectedPeers)
//        {
//            peers = unconnectedPeers.Take(UNCONNECTED_MAX).ToArray();
//        }
//        using (BinaryWriter writer = new BinaryWriter(stream, Encoding.ASCII, true))
//        {
//            writer.Write(peers.Length);
//            foreach (IPEndPoint endpoint in peers)
//            {
//                writer.Write(endpoint.Address.MapToIPv4().GetAddressBytes());
//                writer.Write((ushort)endpoint.Port);
//            }
//        }
    }

    public void Start()
    {
        Start(DEFAULT_PORT);
    }

    public void Start(int port)
    {
//        if (Interlocked.Exchange(ref started, 1) == 0)
//        {
//            IPAddress address = LocalAddresses.FirstOrDefault(p => p.AddressFamily == AddressFamily.InterNetwork && !IsIntranetAddress(p));
//            if (address == null && UpnpEnabled && UPnP.Discover())
//            {
//                try
//                {
//                    address = UPnP.GetExternalIP();
//                    UPnP.ForwardPort(port, ProtocolType.Tcp, "AntShares");
//                    LocalAddresses.Add(address);
//                }
//                catch { }
//            }
//            listener = new TcpListener(IPAddress.Any, port);
//            try
//            {
//                listener.Start();
//                Port = (ushort)port;
//            }
//            catch (SocketException) { }
//            connectThread.Start();
//            if (Port > 0) listenerThread.Start();
//        }
    }

    public void SynchronizeMemoryPool()
    {
        synchronized (connectedPeers)
        {
            for (RemoteNode node : connectedPeers)
            {
                // TODO
                //node.RequestMemoryPool();
            }
        }
    }

}
