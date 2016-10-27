package OnChain.Network.Payloads;

import OnChain.IO.*;

public class AddrPayload implements Serializable
{
    public Object /* TODO NetworkAddressWithTime*/[] AddressList;

    private AddrPayload(Object[] addressList) {
        super();
        AddressList = addressList;
    }

    public static AddrPayload Create(/*NetworkAddressWithTime*/ Object[] addresses)
    {
        return new AddrPayload(addresses);
    }

    @Override
    public void deserialize(BinaryReader reader)
    {
        // TODO
        //this.AddressList = reader.ReadSerializableArray<NetworkAddressWithTime>();
    }

    @Override
    public void serialize(BinaryWriter writer)
    {
        // TODO
        //writer.Write(AddressList);
    }
}