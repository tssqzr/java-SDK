package OnChain.Core.Scripts;

import java.io.IOException;

import OnChain.*;
import OnChain.Cryptography.Digest;
import OnChain.IO.*;
import OnChain.IO.Json.*;

/**
 *  脚本
 */
public class Script implements Serializable
{
    public byte[] stackScript;
    public byte[] redeemScript;

    @Override
    public void deserialize(BinaryReader reader) throws IOException
    {
        stackScript = reader.readVarBytes();
        redeemScript = reader.readVarBytes();
    }

    @Override
    public void serialize(BinaryWriter writer) throws IOException
    {
        writer.writeVarBytes(stackScript);
        writer.writeVarBytes(redeemScript);
    }

    /**
     *  变成json对象
     *  <returns>返回json对象</returns>
     */
    public JObject json()
    {
        JObject json = new JObject();
        json.set("stack", new JString(Helper.toHexString(stackScript)));
        json.set("redeem", new JString(Helper.toHexString(redeemScript)));
        return json;
    }

    public static UInt160 toScriptHash(byte[] script)
    {
    	return new UInt160(Digest.hash160(script));
    }
}
