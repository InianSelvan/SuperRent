import com.superrent.gui.SuperRent;
import java.lang.reflect.*;

/** 
Compile with this:
C:\Documents and Settings\glow\My Documents\j>javac DumpMethods.java

Run like this, and results follow
C:\Documents and Settings\glow\My Documents\j>java DumpMethods
public void DumpMethods.foo()
public int DumpMethods.bar()
public java.lang.String DumpMethods.baz()
public static void DumpMethods.main(java.lang.String[])
*/

public class DumpMethods {



    public static void main(String args[])
    {
        try {
            Class c = SuperRent.class;
            Field[] m = c.getDeclaredFields();
            for (int i = 0; i < m.length; i++)
            System.out.println(m[i].toString());
        } catch (Throwable e) {
            System.err.println(e);
        }
    }
}