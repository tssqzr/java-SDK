package OnChain.Implementations.Wallets.SQLite;

import java.io.File;
import java.nio.*;
import java.sql.Date;
import java.util.*;

import javax.crypto.*;

import OnChain.*;
import OnChain.Core.*;
import OnChain.IO.Serializable;
import OnChain.Wallets.*;

public class UserWallet extends Wallet
{
    // TODO
    //public event EventHandler<Iterable<TransactionInfo>> TransactionsChanged;

    protected UserWallet(String path, String password, boolean create) throws BadPaddingException, IllegalBlockSizeException
    {
        super(path, password, create);
    }

    @Override
    public void addContract(OnChain.Wallets.Contract contract)
    {
        super.addContract(contract);
        try (WalletDataContext ctx = new WalletDataContext(dbPath()))
        {
        	Contract entity = new Contract();
        	entity.scriptHash = contract.scriptHash().toArray();
        	entity.publicKeyHash = contract.publicKeyHash.toArray();
        	entity.rawData = contract.toArray();
        	ctx.insertOrUpdate(entity);
        }
    }

    @Override
    protected void buildDatabase()
    {
    	File file = new File(dbPath());
    	file.delete();
    }

    public static UserWallet create(String path, String password)
    {
        UserWallet wallet;
		try
		{
			wallet = new UserWallet(path, password, true);
		}
		catch (BadPaddingException | IllegalBlockSizeException ex)
		{
			throw new RuntimeException(ex);
		}
        wallet.createAccount();
        return wallet;
    }

    @Override
    public OnChain.Wallets.Account createAccount(byte[] privateKey)
    {
    	OnChain.Wallets.Account account = super.createAccount(privateKey);
        onCreateAccount(account);
        addContract(OnChain.Wallets.Contract.createSignatureContract(account.publicKey));
        return account;
    }

    @Override
    public boolean deleteAccount(UInt160 publicKeyHash)
    {
        boolean flag = super.deleteAccount(publicKeyHash);
        if (flag)
        {
            try (WalletDataContext ctx = new WalletDataContext(dbPath()))
            {
            	ctx.deleteAccount(publicKeyHash);
            }
        }
        return flag;
    }

    @Override
    public boolean deleteContract(UInt160 scriptHash)
    {
        boolean flag = super.deleteContract(scriptHash);
        if (flag)
        {
            try (WalletDataContext ctx = new WalletDataContext(dbPath()))
            {
            	ctx.deleteContract(scriptHash);
            }
        }
        return flag;
    }

    @Override
    public OnChain.Wallets.Coin[] findUnspentCoins(UInt256 asset_id, Fixed8 amount)
    {
    	OnChain.Wallets.Coin[] coins = findUnspentCoins(Arrays.stream(findUnspentCoins()).filter(p -> getContract(p.scriptHash).isStandard()), asset_id, amount);
    	if (coins == null) coins = super.findUnspentCoins(asset_id, amount);
        return coins;
    }

    //TODO
//    private static Iterable<TransactionInfo> GetTransactionInfo(Iterable<Transaction> transactions)
//    {
//        return transactions.Select(p => new TransactionInfo
//        {
//            Transaction = AntShares.Core.Transaction.DeserializeFrom(p.RawData),
//            Height = p.Height,
//            Time = p.Time
//        });
//    }

    //TODO
//    public static Version GetVersion(String path)
//    {
//        byte[] buffer;
//        using (WalletDataContext ctx = new WalletDataContext(path))
//        {
//            buffer = ctx.Keys.FirstOrDefault(p => p.Name == "Version")?.Value;
//        }
//        if (buffer == null) return new Version();
//        int major = BitConverter.ToInt32(buffer, 0);
//        int minor = BitConverter.ToInt32(buffer, 4);
//        int build = BitConverter.ToInt32(buffer, 8);
//        int revision = BitConverter.ToInt32(buffer, 12);
//        return new Version(major, minor, build, revision);
//    	  return new Version();
//    }

    @Override
    protected OnChain.Wallets.Account[] loadAccounts()
    {
        try (WalletDataContext ctx = new WalletDataContext(dbPath()))
        {
        	Account[] entities = ctx.getAccounts();
        	OnChain.Wallets.Account[] accounts = new OnChain.Wallets.Account[entities.length];
        	for (int i = 0; i < accounts.length; i++)
        	{
        		byte[] decryptedPrivateKey = decryptPrivateKey(entities[i].privateKeyEncrypted);
        		accounts[i] = new OnChain.Wallets.Account(decryptedPrivateKey);
        		Arrays.fill(decryptedPrivateKey, (byte)0);
        	}
        	return accounts;
        }
        catch (IllegalBlockSizeException | BadPaddingException ex)
        {
			throw new RuntimeException(ex);
		}
    }

    @Override
    protected OnChain.Wallets.Coin[] loadCoins()
    {
        try (WalletDataContext ctx = new WalletDataContext(dbPath()))
        {
        	Coin[] entities = ctx.getCoins();
        	OnChain.Wallets.Coin[] coins = new OnChain.Wallets.Coin[entities.length];
        	for (int i = 0; i < coins.length; i++)
        	{
        		coins[i] = new OnChain.Wallets.Coin();
        		coins[i].input = new TransactionInput();
        		coins[i].input.prevHash = new UInt256(entities[i].txid);
        		coins[i].input.prevIndex = (short)entities[i].index;
        		coins[i].assetId = new UInt256(entities[i].assetId);
        		coins[i].value = new Fixed8(entities[i].value);
        		coins[i].scriptHash = new UInt160(entities[i].scriptHash);
        		coins[i].setState(CoinState.values()[entities[i].state]);
        	}
        	return coins;
        }
    }

    @Override
    protected OnChain.Wallets.Contract[] loadContracts()
    {
        try (WalletDataContext ctx = new WalletDataContext(dbPath()))
        {
        	Contract[] entities = ctx.getContracts();
        	OnChain.Wallets.Contract[] contracts = new OnChain.Wallets.Contract[entities.length];
        	for (int i = 0; i < contracts.length; i++)
        	{
        		contracts[i] = Serializable.from(entities[i].rawData, OnChain.Wallets.Contract.class);
        	}
        	return contracts;
        }
        catch (InstantiationException | IllegalAccessException ex)
        {
        	throw new RuntimeException(ex);
		}
    }

    @Override
    protected byte[] loadStoredData(String name)
    {
        try (WalletDataContext ctx = new WalletDataContext(dbPath()))
        {
            Key entity = ctx.getKey(name);
            if (entity == null) return null;
            return entity.value;
        }
    }

    //TODO
//    public Iterable<TransactionInfo> LoadTransactions()
//    {
//        using (WalletDataContext ctx = new WalletDataContext(DbPath))
//        {
//            return GetTransactionInfo(ctx.Transactions.ToArray());
//        }
//    }

    //TODO
//    public static void Migrate(String path_old, String path_new)
//    {
//        Version current_version = Assembly.GetExecutingAssembly().GetName().Version;
//        using (WalletDataContext ctx_old = new WalletDataContext(path_old))
//        using (WalletDataContext ctx_new = new WalletDataContext(path_new))
//        {
//            ctx_new.Database.EnsureCreated();
//            ctx_new.Accounts.AddRange(ctx_old.Accounts);
//            ctx_new.Contracts.AddRange(ctx_old.Contracts);
//            ctx_new.Keys.AddRange(ctx_old.Keys.Where(p => p.Name != "Height" && p.Name != "Version"));
//            SaveStoredData(ctx_new, "Height", BitConverter.GetBytes(0));
//            SaveStoredData(ctx_new, "Version", new[] { current_version.Major, current_version.Minor, current_version.Build, current_version.Revision }.Select(p => BitConverter.GetBytes(p)).SelectMany(p => p).ToArray());
//            ctx_new.SaveChanges();
//        }
//    }

    private void onCoinsChanged(WalletDataContext ctx, OnChain.Wallets.Coin[] added, OnChain.Wallets.Coin[] changed, OnChain.Wallets.Coin[] deleted)
    {
    	ctx.insert(Arrays.stream(added).map(p ->
    	{
    		Coin entity = new Coin();
    		entity.txid = p.input.prevHash.toArray();
    		entity.index = Short.toUnsignedInt(p.input.prevIndex);
    		entity.assetId = p.assetId.toArray();
    		entity.value = p.value.getData();
    		entity.scriptHash = p.scriptHash.toArray();
    		entity.state = CoinState.Unspent.ordinal();
    		return entity;
    	}).toArray(Coin[]::new));
    	ctx.update(Arrays.stream(changed).map(p ->
    	{
    		Coin entity = new Coin();
    		entity.txid = p.input.prevHash.toArray();
    		entity.index = Short.toUnsignedInt(p.input.prevIndex);
    		entity.state = p.getState().ordinal();
    		return entity;
    	}).toArray(Coin[]::new));
    	ctx.delete(Arrays.stream(deleted).map(p ->
    	{
    		Coin entity = new Coin();
    		entity.txid = p.input.prevHash.toArray();
    		entity.index = Short.toUnsignedInt(p.input.prevIndex);
    		return entity;
    	}).toArray(Coin[]::new));
    }

    private void onCreateAccount(OnChain.Wallets.Account account)
    {
        byte[] decryptedPrivateKey = new byte[96];
        System.arraycopy(account.publicKey.getEncoded(false), 1, decryptedPrivateKey, 0, 64);
        //using (account.Decrypt())
        {
        	System.arraycopy(account.privateKey, 0, decryptedPrivateKey, 64, 32);
        }
    	Account entity = new Account();
    	entity.privateKeyEncrypted = encryptPrivateKey(decryptedPrivateKey);
    	entity.publicKeyHash = account.publicKeyHash.toArray();
        Arrays.fill(decryptedPrivateKey, (byte)0);
        try (WalletDataContext ctx = new WalletDataContext(dbPath()))
        {
        	ctx.insertOrUpdate(entity);
        }
    }

    @Override
    protected void onProcessNewBlock(Block block, OnChain.Wallets.Coin[] added, OnChain.Wallets.Coin[] changed, OnChain.Wallets.Coin[] deleted)
    {
    	ArrayList<OnChain.Core.Transaction> tx_changed = new ArrayList<OnChain.Core.Transaction>();
        try (WalletDataContext ctx = new WalletDataContext(dbPath()))
        {
        	ctx.beginTransaction();
            for (OnChain.Core.Transaction tx : block.transactions)
            {
            	if (isWalletTransaction(tx))
            	{
            		tx_changed.add(tx);
            		Transaction entity = new Transaction();
            		entity.hash = tx.hash().toArray();
            		entity.type = tx.type.value();
            		entity.rawData = tx.toArray();
            		entity.height = block.height;
            		entity.time = new Date(block.timestamp * 1000);
            		ctx.insertOrUpdate(entity);
            	}
            }
            onCoinsChanged(ctx, added, changed, deleted);
            if (tx_changed.size() > 0 || added.length > 0 || changed.length > 0 || deleted.length > 0)
            {
            	saveStoredData(ctx, "Height", ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(walletHeight()).array());
	            ctx.commit();
            }
        }
        //TODO
//        if (tx_changed.Length > 0)
//            TransactionsChanged?.Invoke(this, GetTransactionInfo(tx_changed));
    }

    @Override
    protected void onSaveTransaction(OnChain.Core.Transaction tx, OnChain.Wallets.Coin[] added, OnChain.Wallets.Coin[] changed)
    {
        Transaction tx_changed = null;
        try (WalletDataContext ctx = new WalletDataContext(dbPath()))
        {
        	ctx.beginTransaction();
        	tx_changed = new Transaction();
        	tx_changed.hash = tx.hash().toArray();
        	tx_changed.type = tx.type.value();
        	tx_changed.rawData = tx.toArray();
        	tx_changed.height = -1;
        	tx_changed.time = new Date(System.currentTimeMillis());
    		ctx.insertOrUpdate(tx_changed);
            onCoinsChanged(ctx, added, changed, new OnChain.Wallets.Coin[0]);
            ctx.commit();
        }
        //TODO
        //TransactionsChanged?.Invoke(this, GetTransactionInfo(new[] { tx_changed }));
    }

    public static UserWallet open(String path, String password) throws BadPaddingException, IllegalBlockSizeException
    {
        return new UserWallet(path, password, false);
    }

    @Override
    public void rebuild()
    {
        synchronized (syncroot)
        {
            super.rebuild();
            //TODO
//            using (WalletDataContext ctx = new WalletDataContext(DbPath))
//            {
//                ctx.Keys.First(p => p.Name == "Height").Value = BitConverter.GetBytes(0);
//                ctx.Database.ExecuteSqlCommand($"DELETE FROM [{nameof(Transaction)}]");
//                ctx.Database.ExecuteSqlCommand($"DELETE FROM [{nameof(Coin)}]");
//                ctx.SaveChanges();
//            }
        }
    }

    @Override
    protected void saveStoredData(String name, byte[] value)
    {
        try (WalletDataContext ctx = new WalletDataContext(dbPath()))
        {
            saveStoredData(ctx, name, value);
        }
    }

    private static void saveStoredData(WalletDataContext ctx, String name, byte[] value)
    {
    	Key entity = new Key();
    	entity.name = name;
    	entity.value = value;
    	ctx.insertOrUpdate(entity);
    }
}
