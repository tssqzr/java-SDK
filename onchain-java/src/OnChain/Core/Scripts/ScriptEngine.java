package OnChain.Core.Scripts;
//package AntShares.Core.Scripts;
//
//import java.io.ByteArrayInputStream;
//import java.io.IOException;
//import java.util.Stack;
//
//import org.bouncycastle.math.ec.ECCurve;
//import org.bouncycastle.math.ec.ECPoint;
//
//import AntShares.Core.Signable;
//import AntShares.IO.BinaryReader;
//
//public class ScriptEngine
//{
//    private static final int MAXSTEPS = 1200;
//    private static final int ECDSA_PUBLIC_P256_MAGIC = 0x31534345;
//
//    private final Script script;
//    private final Signable signable;
//    private final byte[] hash;
//
//    private InterfaceEngine iEngine = null;
//    private final Stack<StackItem> stack = new Stack<StackItem>();
//    private final Stack<StackItem> altStack = new Stack<StackItem>();
//    private final Stack<Boolean> vfExec = new Stack<Boolean>();
//    private int nOpCount = 0;
//
//    public ScriptEngine(Script script, Signable signable)
//    {
//        this.script = script;
//        this.signable = signable;
//        this.hash = null;// TODO signable.getHashForSigning();
//    }
//
//    public boolean Execute()
//    {
//        try
//        {
//            if (!ExecuteScript(script.stackScript, true)) return false;
//            if (!ExecuteScript(script.redeemScript, false)) return false;
//        }
//        catch (Exception ex)// TODO when (ex is FormatException || ex is InvalidCastException)
//        {
//            return false;
//        }
//        return stack.size() == 1;// TODO && stack.pop();
//    }
//
//    private boolean ExecuteOp(ScriptOp opcode, BinaryReader opReader)
//    {
//        boolean fExec = vfExec.stream().allMatch(p -> p);
//        // TODO possibly bug of comparing signed byte value.
//        if (!fExec && (opcode.getByte() < ScriptOp.OP_IF.getByte() || opcode.getByte() > ScriptOp.OP_ENDIF.getByte()))
//            return true;
//        if (opcode.getByte() > ScriptOp.OP_16.getByte() && ++nOpCount > MAXSTEPS) return false;
//        // TODO
//        //int remain = (int)(opReader.BaseStream.Length - opReader.BaseStream.Position);
//        if (opcode.getByte() >= ScriptOp.OP_PUSHBYTES1.getByte() && opcode.getByte() <= ScriptOp.OP_PUSHBYTES75.getByte())
//        {
//            //if (remain < opcode.getByte()) return false;
//            //stack.push(opReader.readBytes(opcode.getByte()));
//            return true;
//        }
//        switch (opcode)
//        {
//            // Push value
//            case OP_0:
//                //stack.push(new byte[0]);
//                break;
//            case OP_PUSHDATA1:
//                {
////                    if (remain < 1) return false;
////                    byte length = opReader.readByte();
////                    if (remain - 1 < length) return false;
////                    stack.push(opReader.readBytes(length));
//                }
//                break;
//            case OP_PUSHDATA2:
//                {
////                    if (remain < 2) return false;
////                    short length = opReader.readShort();
////                    if (remain - 2 < length) return false;
////                    stack.push(opReader.readBytes(length));
//                }
//                break;
//            case OP_PUSHDATA4:
//                {
////                    if (remain < 4) return false;
////                    int length = opReader.ReadInt32();
////                    if (remain - 4 < length) return false;
////                    stack.push(opReader.readBytes(length));
//                }
//                break;
//            case OP_1NEGATE:
//            case OP_1:
//            case OP_2:
//            case OP_3:
//            case OP_4:
//            case OP_5:
//            case OP_6:
//            case OP_7:
//            case OP_8:
//            case OP_9:
//            case OP_10:
//            case OP_11:
//            case OP_12:
//            case OP_13:
//            case OP_14:
//            case OP_15:
//            case OP_16:
////                stack.push(opcode - ScriptOp.OP_1 + 1);
//                break;
//
//            // Control
//            case OP_NOP:
//                break;
//            case OP_CALL:
////                if (remain < 1) return false;
////                if (iEngine == null)
////                    iEngine = new InterfaceEngine(stack, altStack, signable);
////                return iEngine.ExecuteOp((InterfaceOp)opReader.readByte());
//            case OP_IF:
//            case OP_NOTIF:
//                {
////                    boolean fValue = false;
////                    if (fExec)
////                    {
////                        if (stack.size() < 1) return false;
////                        fValue = stack.Pop();
////                        if (opcode == ScriptOp.OP_NOTIF)
////                            fValue = !fValue;
////                    }
////                    vfExec.Push(fValue);
//                }
//                break;
//            case OP_ELSE:
////                if (vfExec.size() == 0) return false;
////                vfExec.Push(!vfExec.Pop());
//                break;
//            case OP_ENDIF:
////                if (vfExec.size() == 0) return false;
////                vfExec.Pop();
//                break;
//            case OP_VERIFY:
////                if (stack.size() < 1) return false;
////                if (stack.Peek().GetBooleanArray().All(p => p))
////                    stack.Pop();
////                else
////                    return false;
//                break;
//            case OP_RETURN:
//                return false;
//
//            // Stack ops
//            case OP_TOALTSTACK:
////                if (stack.size() < 1) return false;
////                altStack.Push(stack.Pop());
//                break;
//            case OP_FROMALTSTACK:
////                if (altStack.size() < 1) return false;
////                stack.push(altStack.Pop());
//                break;
//            case OP_2DROP:
////                if (stack.size() < 2) return false;
////                stack.Pop();
////                stack.Pop();
//                break;
//            case OP_2DUP:
//                {
////                    if (stack.size() < 2) return false;
////                    StackItem x2 = stack.Pop();
////                    StackItem x1 = stack.Peek();
////                    stack.push(x2);
////                    stack.push(x1);
////                    stack.push(x2);
//                }
//                break;
//            case OP_3DUP:
//                {
////                    if (stack.size() < 3) return false;
////                    StackItem x3 = stack.Pop();
////                    StackItem x2 = stack.Pop();
////                    StackItem x1 = stack.Peek();
////                    stack.push(x2);
////                    stack.push(x3);
////                    stack.push(x1);
////                    stack.push(x2);
////                    stack.push(x3);
//                }
//                break;
//            case OP_2OVER:
//                {
////                    if (stack.size() < 4) return false;
////                    StackItem x4 = stack.Pop();
////                    StackItem x3 = stack.Pop();
////                    StackItem x2 = stack.Pop();
////                    StackItem x1 = stack.Peek();
////                    stack.push(x2);
////                    stack.push(x3);
////                    stack.push(x4);
////                    stack.push(x1);
////                    stack.push(x2);
//                }
//                break;
//            case OP_2ROT:
////                {
////                    if (stack.size() < 6) return false;
////                    StackItem x6 = stack.Pop();
////                    StackItem x5 = stack.Pop();
////                    StackItem x4 = stack.Pop();
////                    StackItem x3 = stack.Pop();
////                    StackItem x2 = stack.Pop();
////                    StackItem x1 = stack.Pop();
////                    stack.push(x3);
////                    stack.push(x4);
////                    stack.push(x5);
////                    stack.push(x6);
////                    stack.push(x1);
////                    stack.push(x2);
////                }
//                break;
//            case OP_2SWAP:
////                {
////                    if (stack.size() < 4) return false;
////                    StackItem x4 = stack.Pop();
////                    StackItem x3 = stack.Pop();
////                    StackItem x2 = stack.Pop();
////                    StackItem x1 = stack.Pop();
////                    stack.push(x3);
////                    stack.push(x4);
////                    stack.push(x1);
////                    stack.push(x2);
////                }
//                break;
//            case OP_IFDUP:
////                if (stack.size() < 1) return false;
////                if (stack.Peek())
////                    stack.push(stack.Peek());
//                break;
//            case OP_DEPTH:
////                stack.push(stack.size());
//                break;
//            case OP_DROP:
////                if (stack.size() < 1) return false;
////                stack.Pop();
//                break;
//            case OP_DUP:
////                if (stack.size() < 1) return false;
////                stack.push(stack.Peek());
//                break;
//            case OP_NIP:
////                {
////                    if (stack.size() < 2) return false;
////                    StackItem x2 = stack.Pop();
////                    stack.Pop();
////                    stack.push(x2);
////                }
//                break;
//            case OP_OVER:
////                {
////                    if (stack.size() < 2) return false;
////                    StackItem x2 = stack.Pop();
////                    StackItem x1 = stack.Peek();
////                    stack.push(x2);
////                    stack.push(x1);
////                }
//                break;
//            case OP_PICK:
////                {
////                    if (stack.size() < 2) return false;
////                    int n = (int)(BigInteger)stack.Pop();
////                    if (n < 0) return false;
////                    if (stack.size() < n + 1) return false;
////                    StackItem[] buffer = new StackItem[n];
////                    for (int i = 0; i < n; i++)
////                        buffer[i] = stack.Pop();
////                    StackItem xn = stack.Peek();
////                    for (int i = n - 1; i >= 0; i--)
////                        stack.push(buffer[i]);
////                    stack.push(xn);
////                }
//                break;
//            case OP_ROLL:
////                {
////                    if (stack.size() < 2) return false;
////                    int n = (int)(BigInteger)stack.Pop();
////                    if (n < 0) return false;
////                    if (n == 0) return true;
////                    if (stack.size() < n + 1) return false;
////                    StackItem[] buffer = new StackItem[n];
////                    for (int i = 0; i < n; i++)
////                        buffer[i] = stack.Pop();
////                    StackItem xn = stack.Pop();
////                    for (int i = n - 1; i >= 0; i--)
////                        stack.push(buffer[i]);
////                    stack.push(xn);
////                }
//                break;
//            case OP_ROT:
////                {
////                    if (stack.size() < 3) return false;
////                    StackItem x3 = stack.Pop();
////                    StackItem x2 = stack.Pop();
////                    StackItem x1 = stack.Pop();
////                    stack.push(x2);
////                    stack.push(x3);
////                    stack.push(x1);
////                }
//                break;
//            case OP_SWAP:
////                {
////                    if (stack.size() < 2) return false;
////                    StackItem x2 = stack.Pop();
////                    StackItem x1 = stack.Pop();
////                    stack.push(x2);
////                    stack.push(x1);
////                }
//                break;
//            case OP_TUCK:
////                {
////                    if (stack.size() < 2) return false;
////                    StackItem x2 = stack.Pop();
////                    StackItem x1 = stack.Pop();
////                    stack.push(x2);
////                    stack.push(x1);
////                    stack.push(x2);
////                }
//                break;
//            case OP_CAT:
////                {
////                    if (stack.size() < 2) return false;
////                    StackItem x2 = stack.Pop();
////                    StackItem x1 = stack.Pop();
////                    byte[][] b1 = x1.GetBytesArray();
////                    byte[][] b2 = x2.GetBytesArray();
////                    if (b1.Length != b2.Length) return false;
////                    byte[][] r = b1.Zip(b2, (p1, p2) => p1.Concat(p2).ToArray()).ToArray();
////                    if (x1.IsArray || x2.IsArray)
////                        stack.push(r);
////                    else
////                        stack.push(r[0]);
////                }
//                break;
//            case OP_SUBSTR:
////                {
////                    if (stack.size() < 3) return false;
////                    int count = (int)(BigInteger)stack.Pop();
////                    if (count < 0) return false;
////                    int index = (int)(BigInteger)stack.Pop();
////                    if (index < 0) return false;
////                    StackItem x = stack.Pop();
////                    byte[][] s = x.GetBytesArray();
////                    s = s.Select(p => p.Skip(index).Take(count).ToArray()).ToArray();
////                    if (x.IsArray)
////                        stack.push(s);
////                    else
////                        stack.push(s[0]);
////                }
//                break;
//            case OP_LEFT:
////                {
////                    if (stack.size() < 2) return false;
////                    int count = (int)(BigInteger)stack.Pop();
////                    if (count < 0) return false;
////                    StackItem x = stack.Pop();
////                    byte[][] s = x.GetBytesArray();
////                    s = s.Select(p => p.Take(count).ToArray()).ToArray();
////                    if (x.IsArray)
////                        stack.push(s);
////                    else
////                        stack.push(s[0]);
////                }
//                break;
//            case OP_RIGHT:
////                {
////                    if (stack.size() < 2) return false;
////                    int count = (int)(BigInteger)stack.Pop();
////                    if (count < 0) return false;
////                    StackItem x = stack.Pop();
////                    byte[][] s = x.GetBytesArray();
////                    if (s.Any(p => p.Length < count)) return false;
////                    s = s.Select(p => p.Skip(p.Length - count).ToArray()).ToArray();
////                    if (x.IsArray)
////                        stack.push(s);
////                    else
////                        stack.push(s[0]);
////                }
//                break;
//            case OP_SIZE:
////                {
////                    if (stack.size() < 1) return false;
////                    StackItem x = stack.Peek();
////                    int[] r = x.GetBytesArray().Select(p => p.Length).ToArray();
////                    if (x.IsArray)
////                        stack.push(r);
////                    else
////                        stack.push(r[0]);
////                }
//                break;
////
////            // Bitwise logic
//            case OP_INVERT:
////                {
////                    if (stack.size() < 1) return false;
////                    StackItem x = stack.Pop();
////                    BigInteger[] r = x.GetIntArray().Select(p => ~p).ToArray();
////                    if (x.IsArray)
////                        stack.push(r);
////                    else
////                        stack.push(r[0]);
////                }
//                break;
//            case OP_AND:
////                {
////                    if (stack.size() < 2) return false;
////                    StackItem x2 = stack.Pop();
////                    StackItem x1 = stack.Pop();
////                    BigInteger[] b1 = x1.GetIntArray();
////                    BigInteger[] b2 = x2.GetIntArray();
////                    if (b1.Length != b2.Length) return false;
////                    BigInteger[] r = b1.Zip(b2, (p1, p2) => p1 & p2).ToArray();
////                    if (x1.IsArray || x2.IsArray)
////                        stack.push(r);
////                    else
////                        stack.push(r[0]);
////                }
//                break;
//            case OP_OR:
////                {
////                    if (stack.size() < 2) return false;
////                    StackItem x2 = stack.Pop();
////                    StackItem x1 = stack.Pop();
////                    BigInteger[] b1 = x1.GetIntArray();
////                    BigInteger[] b2 = x2.GetIntArray();
////                    if (b1.Length != b2.Length) return false;
////                    BigInteger[] r = b1.Zip(b2, (p1, p2) => p1 | p2).ToArray();
////                    if (x1.IsArray || x2.IsArray)
////                        stack.push(r);
////                    else
////                        stack.push(r[0]);
////                }
//                break;
//            case OP_XOR:
////                {
////                    if (stack.size() < 2) return false;
////                    StackItem x2 = stack.Pop();
////                    StackItem x1 = stack.Pop();
////                    BigInteger[] b1 = x1.GetIntArray();
////                    BigInteger[] b2 = x2.GetIntArray();
////                    if (b1.Length != b2.Length) return false;
////                    BigInteger[] r = b1.Zip(b2, (p1, p2) => p1 ^ p2).ToArray();
////                    if (x1.IsArray || x2.IsArray)
////                        stack.push(r);
////                    else
////                        stack.push(r[0]);
////                }
//                break;
//            case OP_EQUAL:
//            case OP_EQUALVERIFY:
////                {
////                    if (stack.size() < 2) return false;
////                    StackItem x2 = stack.Pop();
////                    StackItem x1 = stack.Pop();
////                    byte[][] b1 = x1.GetBytesArray();
////                    byte[][] b2 = x2.GetBytesArray();
////                    if (b1.Length != b2.Length) return false;
////                    boolean[] r = b1.Zip(b2, (p1, p2) => p1.SequenceEqual(p2)).ToArray();
////                    if (x1.IsArray || x2.IsArray)
////                        stack.push(r);
////                    else
////                        stack.push(r[0]);
////                    if (opcode == ScriptOp.OP_EQUALVERIFY)
////                        return ExecuteOp(ScriptOp.OP_VERIFY, opReader);
////                }
//                break;
////
////            // Numeric
//            case OP_1ADD:
////                {
////                    if (stack.size() < 1) return false;
////                    StackItem x = stack.Pop();
////                    BigInteger[] r = x.GetIntArray().Select(p => p + BigInteger.One).ToArray();
////                    if (x.IsArray)
////                        stack.push(r);
////                    else
////                        stack.push(r[0]);
////                }
//                break;
//            case OP_1SUB:
////                {
////                    if (stack.size() < 1) return false;
////                    StackItem x = stack.Pop();
////                    BigInteger[] r = x.GetIntArray().Select(p => p - BigInteger.One).ToArray();
////                    if (x.IsArray)
////                        stack.push(r);
////                    else
////                        stack.push(r[0]);
////                }
//                break;
//            case OP_2MUL:
////                {
////                    if (stack.size() < 1) return false;
////                    StackItem x = stack.Pop();
////                    BigInteger[] r = x.GetIntArray().Select(p => p << 1).ToArray();
////                    if (x.IsArray)
////                        stack.push(r);
////                    else
////                        stack.push(r[0]);
////                }
//                break;
//            case OP_2DIV:
////                {
////                    if (stack.size() < 1) return false;
////                    StackItem x = stack.Pop();
////                    BigInteger[] r = x.GetIntArray().Select(p => p >> 1).ToArray();
////                    if (x.IsArray)
////                        stack.push(r);
////                    else
////                        stack.push(r[0]);
////                }
//                break;
//            case OP_NEGATE:
////                {
////                    if (stack.size() < 1) return false;
////                    StackItem x = stack.Pop();
////                    BigInteger[] r = x.GetIntArray().Select(p => -p).ToArray();
////                    if (x.IsArray)
////                        stack.push(r);
////                    else
////                        stack.push(r[0]);
////                }
//                break;
//            case OP_ABS:
////                {
////                    if (stack.size() < 1) return false;
////                    StackItem x = stack.Pop();
////                    BigInteger[] r = x.GetIntArray().Select(p => BigInteger.Abs(p)).ToArray();
////                    if (x.IsArray)
////                        stack.push(r);
////                    else
////                        stack.push(r[0]);
////                }
//                break;
//            case OP_NOT:
////                {
////                    if (stack.size() < 1) return false;
////                    StackItem x = stack.Pop();
////                    boolean[] r = x.GetBooleanArray().Select(p => !p).ToArray();
////                    if (x.IsArray)
////                        stack.push(r);
////                    else
////                        stack.push(r[0]);
////                }
//                break;
//            case OP_0NOTEQUAL:
////                {
////                    if (stack.size() < 1) return false;
////                    StackItem x = stack.Pop();
////                    boolean[] r = x.GetIntArray().Select(p => p != BigInteger.Zero).ToArray();
////                    if (x.IsArray)
////                        stack.push(r);
////                    else
////                        stack.push(r[0]);
////                }
//                break;
//            case OP_ADD:
////                {
////                    if (stack.size() < 2) return false;
////                    StackItem x2 = stack.Pop();
////                    StackItem x1 = stack.Pop();
////                    BigInteger[] b1 = x1.GetIntArray();
////                    BigInteger[] b2 = x2.GetIntArray();
////                    if (b1.Length != b2.Length) return false;
////                    BigInteger[] r = b1.Zip(b2, (p1, p2) => p1 + p2).ToArray();
////                    if (x1.IsArray || x2.IsArray)
////                        stack.push(r);
////                    else
////                        stack.push(r[0]);
////                }
//                break;
//            case OP_SUB:
////                {
////                    if (stack.size() < 2) return false;
////                    StackItem x2 = stack.Pop();
////                    StackItem x1 = stack.Pop();
////                    BigInteger[] b1 = x1.GetIntArray();
////                    BigInteger[] b2 = x2.GetIntArray();
////                    if (b1.Length != b2.Length) return false;
////                    BigInteger[] r = b1.Zip(b2, (p1, p2) => p1 - p2).ToArray();
////                    if (x1.IsArray || x2.IsArray)
////                        stack.push(r);
////                    else
////                        stack.push(r[0]);
////                }
//                break;
//            case OP_MUL:
////                {
////                    if (stack.size() < 2) return false;
////                    StackItem x2 = stack.Pop();
////                    StackItem x1 = stack.Pop();
////                    BigInteger[] b1 = x1.GetIntArray();
////                    BigInteger[] b2 = x2.GetIntArray();
////                    if (b1.Length != b2.Length) return false;
////                    BigInteger[] r = b1.Zip(b2, (p1, p2) => p1 * p2).ToArray();
////                    if (x1.IsArray || x2.IsArray)
////                        stack.push(r);
////                    else
////                        stack.push(r[0]);
////                }
//                break;
//            case OP_DIV:
////                {
////                    if (stack.size() < 2) return false;
////                    StackItem x2 = stack.Pop();
////                    StackItem x1 = stack.Pop();
////                    BigInteger[] b1 = x1.GetIntArray();
////                    BigInteger[] b2 = x2.GetIntArray();
////                    if (b1.Length != b2.Length) return false;
////                    BigInteger[] r = b1.Zip(b2, (p1, p2) => p1 / p2).ToArray();
////                    if (x1.IsArray || x2.IsArray)
////                        stack.push(r);
////                    else
////                        stack.push(r[0]);
////                }
//                break;
//            case OP_MOD:
////                {
////                    if (stack.size() < 2) return false;
////                    StackItem x2 = stack.Pop();
////                    StackItem x1 = stack.Pop();
////                    BigInteger[] b1 = x1.GetIntArray();
////                    BigInteger[] b2 = x2.GetIntArray();
////                    if (b1.Length != b2.Length) return false;
////                    BigInteger[] r = b1.Zip(b2, (p1, p2) => p1 % p2).ToArray();
////                    if (x1.IsArray || x2.IsArray)
////                        stack.push(r);
////                    else
////                        stack.push(r[0]);
////                }
//                break;
//            case OP_LSHIFT:
////                {
////                    if (stack.size() < 2) return false;
////                    StackItem x2 = stack.Pop();
////                    StackItem x1 = stack.Pop();
////                    BigInteger[] b1 = x1.GetIntArray();
////                    BigInteger[] b2 = x2.GetIntArray();
////                    if (b1.Length != b2.Length) return false;
////                    BigInteger[] r = b1.Zip(b2, (p1, p2) => p1 << (int)p2).ToArray();
////                    if (x1.IsArray || x2.IsArray)
////                        stack.push(r);
////                    else
////                        stack.push(r[0]);
////                }
//                break;
//            case OP_RSHIFT:
////                {
////                    if (stack.size() < 2) return false;
////                    StackItem x2 = stack.Pop();
////                    StackItem x1 = stack.Pop();
////                    BigInteger[] b1 = x1.GetIntArray();
////                    BigInteger[] b2 = x2.GetIntArray();
////                    if (b1.Length != b2.Length) return false;
////                    BigInteger[] r = b1.Zip(b2, (p1, p2) => p1 >> (int)p2).ToArray();
////                    if (x1.IsArray || x2.IsArray)
////                        stack.push(r);
////                    else
////                        stack.push(r[0]);
////                }
//                break;
//            case OP_BOOLAND:
////                {
////                    if (stack.size() < 2) return false;
////                    StackItem x2 = stack.Pop();
////                    StackItem x1 = stack.Pop();
////                    boolean[] b1 = x1.GetBooleanArray();
////                    boolean[] b2 = x2.GetBooleanArray();
////                    if (b1.Length != b2.Length) return false;
////                    boolean[] r = b1.Zip(b2, (p1, p2) => p1 && p2).ToArray();
////                    if (x1.IsArray || x2.IsArray)
////                        stack.push(r);
////                    else
////                        stack.push(r[0]);
////                }
//                break;
//            case OP_BOOLOR:
////                {
////                    if (stack.size() < 2) return false;
////                    StackItem x2 = stack.Pop();
////                    StackItem x1 = stack.Pop();
////                    boolean[] b1 = x1.GetBooleanArray();
////                    boolean[] b2 = x2.GetBooleanArray();
////                    if (b1.Length != b2.Length) return false;
////                    boolean[] r = b1.Zip(b2, (p1, p2) => p1 || p2).ToArray();
////                    if (x1.IsArray || x2.IsArray)
////                        stack.push(r);
////                    else
////                        stack.push(r[0]);
////                }
//                break;
//            case OP_NUMEQUAL:
//            case OP_NUMEQUALVERIFY:
////                {
////                    if (stack.size() < 2) return false;
////                    StackItem x2 = stack.Pop();
////                    StackItem x1 = stack.Pop();
////                    BigInteger[] b1 = x1.GetIntArray();
////                    BigInteger[] b2 = x2.GetIntArray();
////                    if (b1.Length != b2.Length) return false;
////                    boolean[] r = b1.Zip(b2, (p1, p2) => p1 == p2).ToArray();
////                    if (x1.IsArray || x2.IsArray)
////                        stack.push(r);
////                    else
////                        stack.push(r[0]);
////                    if (opcode == ScriptOp.OP_NUMEQUALVERIFY)
////                        return ExecuteOp(ScriptOp.OP_VERIFY, opReader);
////                }
//                break;
//            case OP_NUMNOTEQUAL:
////                {
////                    if (stack.size() < 2) return false;
////                    StackItem x2 = stack.Pop();
////                    StackItem x1 = stack.Pop();
////                    BigInteger[] b1 = x1.GetIntArray();
////                    BigInteger[] b2 = x2.GetIntArray();
////                    if (b1.Length != b2.Length) return false;
////                    boolean[] r = b1.Zip(b2, (p1, p2) => p1 != p2).ToArray();
////                    if (x1.IsArray || x2.IsArray)
////                        stack.push(r);
////                    else
////                        stack.push(r[0]);
////                }
//                break;
//            case OP_LESSTHAN:
////                {
////                    if (stack.size() < 2) return false;
////                    StackItem x2 = stack.Pop();
////                    StackItem x1 = stack.Pop();
////                    BigInteger[] b1 = x1.GetIntArray();
////                    BigInteger[] b2 = x2.GetIntArray();
////                    if (b1.Length != b2.Length) return false;
////                    boolean[] r = b1.Zip(b2, (p1, p2) => p1 < p2).ToArray();
////                    if (x1.IsArray || x2.IsArray)
////                        stack.push(r);
////                    else
////                        stack.push(r[0]);
////                }
//                break;
//            case OP_GREATERTHAN:
////                {
////                    if (stack.size() < 2) return false;
////                    StackItem x2 = stack.Pop();
////                    StackItem x1 = stack.Pop();
////                    BigInteger[] b1 = x1.GetIntArray();
////                    BigInteger[] b2 = x2.GetIntArray();
////                    if (b1.Length != b2.Length) return false;
////                    boolean[] r = b1.Zip(b2, (p1, p2) => p1 > p2).ToArray();
////                    if (x1.IsArray || x2.IsArray)
////                        stack.push(r);
////                    else
////                        stack.push(r[0]);
////                }
//                break;
//            case OP_LESSTHANOREQUAL:
////                {
////                    if (stack.size() < 2) return false;
////                    StackItem x2 = stack.Pop();
////                    StackItem x1 = stack.Pop();
////                    BigInteger[] b1 = x1.GetIntArray();
////                    BigInteger[] b2 = x2.GetIntArray();
////                    if (b1.Length != b2.Length) return false;
////                    boolean[] r = b1.Zip(b2, (p1, p2) => p1 <= p2).ToArray();
////                    if (x1.IsArray || x2.IsArray)
////                        stack.push(r);
////                    else
////                        stack.push(r[0]);
////                }
//                break;
//            case OP_GREATERTHANOREQUAL:
////                {
////                    if (stack.size() < 2) return false;
////                    StackItem x2 = stack.Pop();
////                    StackItem x1 = stack.Pop();
////                    BigInteger[] b1 = x1.GetIntArray();
////                    BigInteger[] b2 = x2.GetIntArray();
////                    if (b1.Length != b2.Length) return false;
////                    boolean[] r = b1.Zip(b2, (p1, p2) => p1 >= p2).ToArray();
////                    if (x1.IsArray || x2.IsArray)
////                        stack.push(r);
////                    else
////                        stack.push(r[0]);
////                }
//                break;
//            case OP_MIN:
////                {
////                    if (stack.size() < 2) return false;
////                    StackItem x2 = stack.Pop();
////                    StackItem x1 = stack.Pop();
////                    BigInteger[] b1 = x1.GetIntArray();
////                    BigInteger[] b2 = x2.GetIntArray();
////                    if (b1.Length != b2.Length) return false;
////                    BigInteger[] r = b1.Zip(b2, (p1, p2) => BigInteger.Min(p1, p2)).ToArray();
////                    if (x1.IsArray || x2.IsArray)
////                        stack.push(r);
////                    else
////                        stack.push(r[0]);
////                }
//                break;
//            case OP_MAX:
////                {
////                    if (stack.size() < 2) return false;
////                    StackItem x2 = stack.Pop();
////                    StackItem x1 = stack.Pop();
////                    BigInteger[] b1 = x1.GetIntArray();
////                    BigInteger[] b2 = x2.GetIntArray();
////                    if (b1.Length != b2.Length) return false;
////                    BigInteger[] r = b1.Zip(b2, (p1, p2) => BigInteger.Max(p1, p2)).ToArray();
////                    if (x1.IsArray || x2.IsArray)
////                        stack.push(r);
////                    else
////                        stack.push(r[0]);
////                }
//                break;
//            case OP_WITHIN:
////                {
////                    if (stack.size() < 3) return false;
////                    BigInteger b = (BigInteger)stack.Pop();
////                    BigInteger a = (BigInteger)stack.Pop();
////                    BigInteger x = (BigInteger)stack.Pop();
////                    stack.push(a <= x && x < b);
////                }
//                break;
////
////            // Crypto
//            case OP_RIPEMD160:
////                {
////                    if (stack.size() < 1) return false;
////                    StackItem x = stack.Pop();
////                    byte[][] r = x.GetBytesArray().Select(p => p.RIPEMD160()).ToArray();
////                    if (x.IsArray)
////                        stack.push(r);
////                    else
////                        stack.push(r[0]);
////                }
//                break;
//            case OP_SHA1:
////                using (SHA1Managed sha = new SHA1Managed())
////                {
////                    if (stack.size() < 1) return false;
////                    StackItem x = stack.Pop();
////                    byte[][] r = x.GetBytesArray().Select(p => sha.ComputeHash(p)).ToArray();
////                    if (x.IsArray)
////                        stack.push(r);
////                    else
////                        stack.push(r[0]);
////                }
//                break;
//            case OP_SHA256:
////                {
////                    if (stack.size() < 1) return false;
////                    StackItem x = stack.Pop();
////                    byte[][] r = x.GetBytesArray().Select(p => p.Sha256()).ToArray();
////                    if (x.IsArray)
////                        stack.push(r);
////                    else
////                        stack.push(r[0]);
////                }
//                break;
//            case OP_HASH160:
////                {
////                    if (stack.size() < 1) return false;
////                    StackItem x = stack.Pop();
////                    byte[][] r = x.GetBytesArray().Select(p => p.Sha256().RIPEMD160()).ToArray();
////                    if (x.IsArray)
////                        stack.push(r);
////                    else
////                        stack.push(r[0]);
////                }
//                break;
//            case OP_HASH256:
////                {
////                    if (stack.size() < 1) return false;
////                    StackItem x = stack.Pop();
////                    byte[][] r = x.GetBytesArray().Select(p => p.Sha256().Sha256()).ToArray();
////                    if (x.IsArray)
////                        stack.push(r);
////                    else
////                        stack.push(r[0]);
////                }
//                break;
//            case OP_CHECKSIG:
//            case OP_CHECKSIGVERIFY:
////                {
////                    if (stack.size() < 2) return false;
////                    byte[] pubkey = (byte[])stack.Pop();
////                    byte[] signature = (byte[])stack.Pop();
////                    stack.push(VerifySignature(hash, signature, pubkey));
////                    if (opcode == ScriptOp.OP_CHECKSIGVERIFY)
////                        return ExecuteOp(ScriptOp.OP_VERIFY, opReader);
////                }
//                break;
//            case OP_CHECKMULTISIG:
//            case OP_CHECKMULTISIGVERIFY:
////                {
////                    if (stack.size() < 4) return false;
////                    int n = (int)(BigInteger)stack.Pop();
////                    if (n < 1) return false;
////                    if (stack.size() < n + 2) return false;
////                    nOpCount += n;
////                    if (nOpCount > MAXSTEPS) return false;
////                    byte[][] pubkeys = new byte[n][];
////                    for (int i = 0; i < n; i++)
////                    {
////                        pubkeys[i] = (byte[])stack.Pop();
////                    }
////                    int m = (int)(BigInteger)stack.Pop();
////                    if (m < 1 || m > n) return false;
////                    if (stack.size() < m) return false;
////                    List<byte[]> signatures = new List<byte[]>();
////                    while (stack.size() > 0)
////                    {
////                        byte[] signature = (byte[])stack.Pop();
////                        if (signature.Length == 0) break;
////                        signatures.Add(signature);
////                    }
////                    if (signatures.size() < m || signatures.size() > n) return false;
////                    boolean fSuccess = true;
////                    for (int i = 0, j = 0; fSuccess && i < signatures.size() && j < n;)
////                    {
////                        if (VerifySignature(hash, signatures[i], pubkeys[j]))
////                            i++;
////                        j++;
////                        if (i >= m) break;
////                        if (signatures.size() - i > n - j)
////                            fSuccess = false;
////                    }
////                    stack.push(fSuccess);
////                    if (opcode == ScriptOp.OP_CHECKMULTISIGVERIFY)
////                        return ExecuteOp(ScriptOp.OP_VERIFY, opReader);
////                }
//                break;
////
////            //case OP_EVAL:
////            //    if (stack.size() < 1) return false;
////            //    if (!ExecuteScript((byte[])stack.Pop(), false))
////            //        return false;
////            //    break;
////
////            // Array
//            case OP_ARRAYSIZE:
////                {
////                    if (stack.size() < 1) return false;
////                    StackItem arr = stack.Pop();
////                    if (arr.IsArray)
////                        stack.push(arr.size());
////                    else
////                        stack.push(1);
////                }
//                break;
//            case OP_PACK:
////                {
////                    if (stack.size() < 1) return false;
////                    int c = (int)(BigInteger)stack.Pop();
////                    if (stack.size() < c) return false;
////                    StackItem[] arr = new StackItem[c];
////                    while (c-- > 0)
////                    {
////                        arr[c] = stack.Pop();
////                        if (arr[c].IsArray) return false;
////                    }
////                    stack.push(new StackItem(arr));
////                }
//                break;
//            case OP_UNPACK:
////                {
////                    if (stack.size() < 1) return false;
////                    StackItem arr = stack.Pop();
////                    if (!arr.IsArray) return false;
////                    foreach (StackItem item in arr)
////                        stack.push(item);
////                    stack.push(arr.size());
////                }
//                break;
//            case OP_DISTINCT:
////                if (stack.size() < 1) return false;
////                stack.push(new StackItem(stack.Pop().Distinct()));
//                break;
//            case OP_SORT:
////                if (stack.size() < 1) return false;
////                stack.push(stack.Pop().GetIntArray().OrderBy(p => p).ToArray());
//                break;
//            case OP_REVERSE:
////                if (stack.size() < 1) return false;
////                stack.push(new StackItem(stack.Pop().Reverse()));
//                break;
//            case OP_CONCAT:
////                {
////                    if (stack.size() < 1) return false;
////                    int c = (int)(BigInteger)stack.Pop();
////                    if (stack.size() < c) return false;
////                    IEnumerable<StackItem> items = Enumerable.Empty<StackItem>();
////                    while (c-- > 0)
////                        items = stack.Pop().Concat(items);
////                    stack.push(new StackItem(items));
////                }
//                break;
//            case OP_UNION:
////                {
////                    if (stack.size() < 1) return false;
////                    int c = (int)(BigInteger)stack.Pop();
////                    if (stack.size() < c) return false;
////                    IEnumerable<StackItem> items = Enumerable.Empty<StackItem>();
////                    while (c-- > 0)
////                        items = stack.Pop().Union(items);
////                    stack.push(new StackItem(items));
////                }
//                break;
//            case OP_INTERSECT:
////                {
////                    if (stack.size() < 1) return false;
////                    int c = (int)(BigInteger)stack.Pop();
////                    if (stack.size() < c) return false;
////                    IEnumerable<StackItem> items = Enumerable.Empty<StackItem>();
////                    while (c-- > 0)
////                        items = stack.Pop().Intersect(items);
////                    stack.push(new StackItem(items));
////                }
//                break;
//            case OP_EXCEPT:
////                {
////                    if (stack.size() < 2) return false;
////                    StackItem x2 = stack.Pop();
////                    StackItem x1 = stack.Pop();
////                    stack.push(new StackItem(x1.Except(x2)));
////                }
//                break;
//            case OP_TAKE:
////                {
////                    if (stack.size() < 2) return false;
////                    int count = (int)(BigInteger)stack.Pop();
////                    stack.push(new StackItem(stack.Pop().Take(count)));
////                }
//                break;
//            case OP_SKIP:
////                {
////                    if (stack.size() < 2) return false;
////                    int count = (int)(BigInteger)stack.Pop();
////                    stack.push(new StackItem(stack.Pop().Skip(count)));
////                }
//                break;
//            case OP_PICKITEM:
////                {
////                    if (stack.size() < 2) return false;
////                    int index = (int)(BigInteger)stack.Pop();
////                    StackItem arr = stack.Pop();
////                    if (arr.size() <= index)
////                        stack.push(new StackItem((byte[])null));
////                    else
////                        stack.push(arr[index]);
////                }
//                break;
//            case OP_ALL:
////                if (stack.size() < 1) return false;
////                stack.push(stack.Pop().All(p => p));
//                break;
//            case OP_ANY:
////                if (stack.size() < 1) return false;
////                stack.push(stack.Pop().Any(p => p));
//                break;
//            case OP_SUM:
////                if (stack.size() < 1) return false;
////                stack.push(stack.Pop().Aggregate(BigInteger.Zero, (s, p) => s + (BigInteger)p));
//                break;
//            case OP_AVERAGE:
////                {
////                    if (stack.size() < 1) return false;
////                    StackItem arr = stack.Pop();
////                    if (arr.size() == 0) return false;
////                    stack.push(arr.Aggregate(BigInteger.Zero, (s, p) => s + (BigInteger)p, p => p / arr.size()));
////                }
//                break;
//            case OP_MAXITEM:
////                if (stack.size() < 1) return false;
////                stack.push(stack.Pop().GetIntArray().Max());
//                break;
//            case OP_MINITEM:
////                if (stack.size() < 1) return false;
////                stack.push(stack.Pop().GetIntArray().Min());
//                break;
////
//            default:
//                return false;
//        }
//        return true;
//    }
//
//    private boolean ExecuteScript(byte[] script, boolean push_only)
//    {
//        ByteArrayInputStream ms = new ByteArrayInputStream(script);
//        BinaryReader opReader = new BinaryReader(ms);
//        while (true) // (opReader.BaseStream.Position < script.Length)
//        {
//            try {
//                ScriptOp opcode = ScriptOp.valueOf(opReader.readByte());
//                if (push_only && opcode.getByte() > ScriptOp.OP_16.getByte()) return false;
//                if (!ExecuteOp(opcode, opReader)) return false;
//            } catch (IOException e) {
//                break;
//            }
//        }
//        return true;
//    }
//
//    private static boolean VerifySignature(byte[] hash, byte[] signature, byte[] pubkey)
//    {
////        try
////        {
////            pubkey = ECPoint.DecodePoint(pubkey, ECCurve.Secp256r1).EncodePoint(false).Skip(1).ToArray();
////        }
////        catch
////        {
////            return false;
////        }
////        pubkey = BitConverter.GetBytes(ECDSA_PUBLIC_P256_MAGIC).Concat(BitConverter.GetBytes(32)).Concat(pubkey).ToArray();
////        using (CngKey key = CngKey.Import(pubkey, CngKeyBlobFormat.EccPublicBlob))
////        using (ECDsaCng ecdsa = new ECDsaCng(key))
////        {
////            return ecdsa.VerifyHash(hash, signature);
////        }
//        return false;
//    }
//}