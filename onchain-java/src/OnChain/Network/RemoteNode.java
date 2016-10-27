﻿package OnChain.Network;

import java.time.Duration;
import java.util.*;

import OnChain.UInt256;
import OnChain.IO.Serializable;
import OnChain.Network.Payloads.*;

public class RemoteNode // TODO : IDisposable
{
    // TODO
    //public event EventHandler<boolean> Disconnected;
    //event EventHandler<Inventory> InventoryReceived;
    //event EventHandler<IPEndPoint[]> PeersReceived;

    private static final Duration OneMinute = Duration.ofMinutes(1);

    private Queue<Message> message_queue = new LinkedList<Message>();
    private static HashSet<UInt256> missions_global = new HashSet<UInt256>();
    private HashSet<UInt256> missions = new HashSet<UInt256>();

    private LocalNode localNode;
    private Thread protocolThread;
    private Thread sendThread;
    // TODO
    private /*TcpClient*/ Object tcp;
    //private NetworkStream stream;
    private boolean connected = false;
    private int disposed = 0;

    private VersionPayload version;// { get; private set; }
    private IPEndPoint RemoteEndpoint;// { get; private set; }
    private IPEndPoint ListenerEndpoint;// { get; private set; }

    public VersionPayload getVersion() {
        return version;
    }
    public IPEndPoint getRemoteEndpoint() {
        return RemoteEndpoint;
    }
    public IPEndPoint getListenerEndpoint() {
        return ListenerEndpoint;
    }

    private RemoteNode(LocalNode localNode)
    {
        this.localNode = localNode;
        this.protocolThread = new Thread(()->RunProtocol());
        this.protocolThread.setDaemon(true);//{ IsBackground = true };
        this.sendThread = new Thread(()->SendLoop());
        this.sendThread.setDaemon(true);//{ IsBackground = true };
    }

    RemoteNode(LocalNode localNode, IPEndPoint remoteEndpoint)
    {
        this(localNode);
        // TODO
        //this.tcp = new TcpClient(remoteEndpoint.Address.IsIPv4MappedToIPv6 ? AddressFamily.InterNetwork : remoteEndpoint.AddressFamily);
        this.ListenerEndpoint = remoteEndpoint;
    }

    RemoteNode(LocalNode localNode, /*TcpClient*/ Object tcp)
    {
        this(localNode);
        this.tcp = tcp;
        OnConnected();
    }

    /*async Task*/ void ConnectAsync()
    {
//        IPAddress address = ListenerEndpoint.Address;
//        if (address.IsIPv4MappedToIPv6)
//            address = address.MapToIPv4();
//        try
//        {
//            await tcp.ConnectAsync(address, ListenerEndpoint.Port);
//            OnConnected();
//        }
//        catch (SocketException)
//        {
//            Disconnect(false);
//            return;
//        }
//        StartProtocol();
    }

    public void Disconnect(boolean error)
    {
//        if (Interlocked.Exchange(ref disposed, 1) == 0)
//        {
//            tcp.Close();
//            Disconnected?.Invoke(this, error);
//            synchronized (missions_global)
//            {
//                foreach (UInt256 hash in missions)
//                {
//                    missions_global.Remove(hash);
//                }
//            }
//            if (protocolThread != Thread.CurrentThread && !protocolThread.ThreadState.HasFlag(ThreadState.Unstarted))
//                protocolThread.Join();
//            if (!sendThread.ThreadState.HasFlag(ThreadState.Unstarted)) sendThread.Join();
//        }
    }

    public void Dispose()
    {
        Disconnect(false);
    }

    private void EnqueueMessage(String command) {
        EnqueueMessage(command, null, false);
    }

    private void EnqueueMessage(String command, Serializable payload, boolean is_single)
    {
        synchronized (message_queue)
        {
//            if (!is_single || message_queue.All(p => p.Command != command))
//            {
//                message_queue.Enqueue(Message.Create(command, payload));
//            }
        }
    }

    private void OnAddrMessageReceived(AddrPayload payload)
    {
//        IPEndPoint[] peers = payload.AddressList.Select(p => p.EndPoint).Where(p => p.Port != localNode.Port || !LocalNode.LocalAddresses.Contains(p.Address)).ToArray();
//        if (peers.Length > 0) PeersReceived?.Invoke(this, peers);
    }

    private void OnConnected()
    {
//        IPEndPoint remoteEndpoint = (IPEndPoint)tcp.Client.RemoteEndPoint;
//        RemoteEndpoint = new IPEndPoint(remoteEndpoint.Address.MapToIPv6(), remoteEndpoint.Port);
//        protocolThread.Name = $"RemoteNode.RunProtocol@{RemoteEndpoint}";
//        sendThread.Name = $"RemoteNode.SendLoop@{RemoteEndpoint}";
//        tcp.SendTimeout = 10000;
//        stream = tcp.GetStream();
        connected = true;
    }

    private void OnGetAddrMessageReceived()
    {
//        if (!localNode.ServiceEnabled) return;
//        AddrPayload payload;
//        synchronized (localNode.connectedPeers)
//        {
//            payload = AddrPayload.Create(localNode.connectedPeers.Where(p => p.ListenerEndpoint != null).Take(100).Select(p => NetworkAddressWithTime.Create(p.ListenerEndpoint, p.Version.Services, p.Version.Timestamp)).ToArray());
//        }
//        EnqueueMessage("addr", payload, true);
    }

    private void OnGetBlocksMessageReceived(GetBlocksPayload payload)
    {
//        if (!localNode.ServiceEnabled) return;
//        if (Blockchain.Default == null) return;
//        if (!Blockchain.Default.Ability.HasFlag(BlockchainAbility.BlockIndexes)) return;
//        UInt256 hash = payload.HashStart.Select(p => Blockchain.Default.GetHeader(p)).Where(p => p != null).OrderBy(p => p.Height).Select(p => p.Hash).FirstOrDefault();
//        if (hash == null || hash == payload.HashStop) return;
//        List<UInt256> hashes = new List<UInt256>();
//        do
//        {
//            hash = Blockchain.Default.GetNextBlockHash(hash);
//            if (hash == null) break;
//            hashes.Add(hash);
//        } while (hash != payload.HashStop && hashes.Count < 500);
//        EnqueueMessage("inv", InvPayload.Create(InventoryType.Block, hashes.ToArray()));
    }

    private void OnGetDataMessageReceived(InvPayload payload)
    {
//        foreach (InventoryVector vector in payload.Inventories.Distinct())
//        {
//            Inventory inventory;
//            if (!localNode.RelayCache.TryGet(vector.Hash, out inventory) && !localNode.ServiceEnabled)
//                continue;
//            switch (vector.Type)
//            {
//                case InventoryType.TX:
//                    if (inventory == null && Blockchain.Default != null)
//                        inventory = Blockchain.Default.GetTransaction(vector.Hash);
//                    if (inventory != null)
//                        EnqueueMessage("tx", inventory);
//                    break;
//                case InventoryType.Block:
//                    if (inventory == null && Blockchain.Default != null)
//                        inventory = Blockchain.Default.GetBlock(vector.Hash);
//                    if (inventory != null)
//                        EnqueueMessage("block", inventory);
//                    break;
//                case InventoryType.Consensus:
//                    if (inventory != null)
//                        EnqueueMessage("consensus", inventory);
//                    break;
//            }
//        }
    }

    private void OnGetHeadersMessageReceived(GetBlocksPayload payload)
    {
//        if (!localNode.ServiceEnabled) return;
//        if (Blockchain.Default == null) return;
//        if (!Blockchain.Default.Ability.HasFlag(BlockchainAbility.BlockIndexes)) return;
//        UInt256 hash = payload.HashStart.Select(p => Blockchain.Default.GetHeader(p)).Where(p => p != null).OrderBy(p => p.Height).Select(p => p.Hash).FirstOrDefault();
//        if (hash == null || hash == payload.HashStop) return;
//        List<Block> headers = new List<Block>();
//        do
//        {
//            hash = Blockchain.Default.GetNextBlockHash(hash);
//            if (hash == null) break;
//            headers.Add(Blockchain.Default.GetHeader(hash));
//        } while (hash != payload.HashStop && headers.Count < 2000);
//        EnqueueMessage("headers", HeadersPayload.Create(headers));
    }

    private void OnHeadersMessageReceived(HeadersPayload payload)
    {
//        if (Blockchain.Default == null) return;
//        Blockchain.Default.AddHeaders(payload.Headers);
//        if (Blockchain.Default.HeaderHeight < Version.StartHeight)
//        {
//            EnqueueMessage("getheaders", GetBlocksPayload.Create(Blockchain.Default.GetLeafHeaderHashes()), true);
//        }
    }

    private void OnInventoryReceived(Inventory inventory)
    {
//        synchronized (missions_global)
//        {
//            missions_global.Remove(inventory.Hash);
//        }
//        missions.Remove(inventory.Hash);
//        if (inventory is MinerTransaction) return;
//        InventoryReceived?.Invoke(this, inventory);
    }

    private void OnInvMessageReceived(InvPayload payload)
    {
//        InventoryVector[] vectors = payload.Inventories.Distinct().Where(p => Enum.IsDefined(typeof(InventoryType), p.Type)).ToArray();
//        synchronized (LocalNode.KnownHashes)
//        {
//            vectors = vectors.Where(p => !LocalNode.KnownHashes.Contains(p.Hash)).ToArray();
//        }
//        if (vectors.Length == 0) return;
//        synchronized (missions_global)
//        {
//            if (localNode.GlobalMissionsEnabled)
//                vectors = vectors.Where(p => !missions_global.Contains(p.Hash)).ToArray();
//            foreach (InventoryVector vector in vectors)
//            {
//                missions_global.Add(vector.Hash);
//                missions.Add(vector.Hash);
//            }
//        }
//        if (vectors.Length == 0) return;
//        EnqueueMessage("getdata", InvPayload.Create(vectors));
    }

    private void OnMemPoolMessageReceived()
    {
//        EnqueueMessage("inv", InvPayload.Create(InventoryType.TX, LocalNode.GetMemoryPool().Select(p => p.Hash).ToArray()));
    }

    private void OnMessageReceived(Message message)
    {
        switch (message.Command)
        {
            case "addr":
                //OnAddrMessageReceived(message.Payload.AsSerializable<AddrPayload>());
                break;
            case "block":
                //OnInventoryReceived(message.Payload.AsSerializable<Block>());
                break;
            case "consensus":
                //OnInventoryReceived(message.Payload.AsSerializable<ConsensusPayload>());
                break;
            case "getaddr":
                OnGetAddrMessageReceived();
                break;
            case "getblocks":
                //OnGetBlocksMessageReceived(message.Payload.AsSerializable<GetBlocksPayload>());
                break;
            case "getdata":
                //OnGetDataMessageReceived(message.Payload.AsSerializable<InvPayload>());
                break;
            case "getheaders":
                //OnGetHeadersMessageReceived(message.Payload.AsSerializable<GetBlocksPayload>());
                break;
            case "headers":
                //OnHeadersMessageReceived(message.Payload.AsSerializable<HeadersPayload>());
                break;
            case "inv":
                //OnInvMessageReceived(message.Payload.AsSerializable<InvPayload>());
                break;
            case "mempool":
                OnMemPoolMessageReceived();
                break;
            case "tx":
                //OnInventoryReceived(Transaction.DeserializeFrom(message.Payload));
                break;
            case "alert":
            case "notfound":
            case "ping":
            case "pong":
            case "reject":
                //暂时忽略
                break;
            case "verack":
            case "version":
            default:
                Disconnect(true);
                break;
        }
    }

    private Message ReceiveMessage(Duration timeout)
    {
//        if (timeout == Timeout.InfiniteTimeSpan) timeout = TimeSpan.Zero;
//        BinaryReader reader = null;
//        try
//        {
//            reader = new BinaryReader(stream, Encoding.UTF8, true);
//            tcp.ReceiveTimeout = (int)timeout.TotalMilliseconds;
//            return reader.ReadSerializable<Message>();
//        }
//        catch (ArgumentException) { }
//        catch (ObjectDisposedException) { }
//        catch (FormatException)
//        {
//            Disconnect(true);
//        }
//        catch (IOException)
//        {
//            Disconnect(false);
//        }
//        finally
//        {
//            if (reader != null) reader.Dispose();
//        }
        return null;
    }

    void Relay(Inventory data)
    {
        // TODO
        //EnqueueMessage("inv", InvPayload.Create(data.getInventoryType(), data.Hash));
    }

    void RequestMemoryPool()
    {
        EnqueueMessage("mempool", null, true);
    }

    void RequestPeers()
    {
        EnqueueMessage("getaddr", null, true);
    }

    private void RunProtocol()
    {
//        if (!SendMessage(Message.Create("version", VersionPayload.Create(localNode.Port, localNode.Nonce, localNode.UserAgent))))
//            return;
//        Message message = ReceiveMessage(TimeSpan.FromSeconds(30));
//        if (message == null) return;
//        if (message.Command != "version")
//        {
//            Disconnect(true);
//            return;
//        }
//        try
//        {
//            Version = message.Payload.AsSerializable<VersionPayload>();
//        }
//        catch (EndOfStreamException)
//        {
//            Disconnect(false);
//            return;
//        }
//        catch (FormatException)
//        {
//            Disconnect(true);
//            return;
//        }
//        if (Version.Nonce == localNode.Nonce)
//        {
//            Disconnect(true);
//            return;
//        }
//        synchronized (localNode.pendingPeers)
//        {
//            synchronized (localNode.connectedPeers)
//            {
//                if (localNode.connectedPeers.Any(p => p.RemoteEndpoint.Address.Equals(RemoteEndpoint.Address) && p.Version.Nonce == Version.Nonce))
//                {
//                    Disconnect(false);
//                    return;
//                }
//                localNode.connectedPeers.Add(this);
//            }
//            if (ListenerEndpoint != null)
//                localNode.pendingPeers.Remove(ListenerEndpoint);
//        }
//        if (ListenerEndpoint != null)
//        {
//            if (ListenerEndpoint.Port != Version.Port)
//            {
//                Disconnect(true);
//                return;
//            }
//        }
//        else if (Version.Port > 0)
//        {
//            ListenerEndpoint = new IPEndPoint(RemoteEndpoint.Address, Version.Port);
//        }
//        if (!SendMessage(Message.Create("verack"))) return;
//        message = ReceiveMessage(TimeSpan.FromSeconds(30));
//        if (message == null) return;
//        if (message.Command != "verack")
//        {
//            Disconnect(true);
//            return;
//        }
//        if (Blockchain.Default?.HeaderHeight < Version.StartHeight)
//        {
//            HashSet<UInt256> hashes = new HashSet<UInt256>(Blockchain.Default.GetLeafHeaderHashes());
//            hashes.UnionWith(hashes.Select(p => Blockchain.Default.GetHeader(p).PrevBlock).ToArray());
//            EnqueueMessage("getheaders", GetBlocksPayload.Create(hashes), true);
//        }
//        sendThread.Start();
//        while (disposed == 0)
//        {
//            if (Blockchain.Default != null && !Blockchain.Default.IsReadOnly)
//            {
//                if (missions.Count == 0 && Blockchain.Default.Height < Version.StartHeight)
//                {
//                    EnqueueMessage("getblocks", GetBlocksPayload.Create(new[] { Blockchain.Default.CurrentBlockHash }), true);
//                }
//            }
//            TimeSpan timeout = missions.Count == 0 ? TimeSpan.FromMinutes(30) : TimeSpan.FromSeconds(60);
//            message = ReceiveMessage(timeout);
//            if (message == null) break;
//            try
//            {
//                OnMessageReceived(message);
//            }
//            catch (EndOfStreamException)
//            {
//                Disconnect(false);
//                break;
//            }
//            catch (FormatException)
//            {
//                Disconnect(true);
//                break;
//            }
//        }
    }

    private void SendLoop()
    {
        while (disposed == 0)
        {
            Message message = null;
            synchronized (message_queue)
            {
                if (message_queue.size() > 0)
                {
                    message = message_queue.poll();
                }
            }
            if (message == null)
            {
                for (int i = 0; i < 10 && disposed == 0; i++)
                {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        // e.printStackTrace();
                    }
                }
            }
            else
            {
                SendMessage(message);
            }
        }
    }

    private boolean SendMessage(Message message)
    {
        if (!connected) throw new IllegalStateException();
        if (disposed > 0) return false;
        // TODO
//        byte[] buffer = message.ToArray();
//        try
//        {
//            stream.Write(buffer, 0, buffer.Length);
//            return true;
//        }
//        catch (ObjectDisposedException) { }
//        catch (IOException)
//        {
//            Disconnect(false);
//        }
        return false;
    }

    void StartProtocol()
    {
        protocolThread.start();
    }
}
