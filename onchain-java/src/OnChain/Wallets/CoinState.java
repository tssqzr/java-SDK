package OnChain.Wallets;

public enum CoinState
{
    Unconfirmed,
    Unspent,
    Spending,
    Spent,
    SpentAndClaimed
}