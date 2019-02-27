//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import java.io.File;
import java.io.FileInputStream;

public class StrategyLoader extends ClassLoader {
    public StrategyLoader() {
    }

    private byte[] readClass(String var1) {
        Object var2 = null;

        try {
            File var3 = new File(var1 + ".class");
            FileInputStream var4 = new FileInputStream(var3);
            byte[] var6 = new byte[(int)var3.length()];
            var4.read(var6);
            return var6;
        } catch (Exception var5) {
            System.out.println("ERROR:  Strategy " + var1 + "could not be read from current directory.\n  Exception is: " + var5 + "\n  Message is: " + var5.getMessage() + "\n  Stack dump follows.\n");
            var5.printStackTrace();
            System.exit(1);
            return (byte[])var2;
        }
    }

    public Class loadClass(String var1) throws ClassNotFoundException, ClassFormatError {
        return this.loadClass(var1, true);
    }

    public Class loadClass(String var1, boolean var2) throws ClassNotFoundException, ClassFormatError {
        try {
            return this.findSystemClass(var1);
        } catch (ClassNotFoundException var6) {
            byte[] var4 = this.readClass(var1);
            if (var4 == null) {
                throw new ClassNotFoundException();
            } else {
                Class var3 = this.defineClass((String)null, var4, 0, var4.length);
                if (var3 == null) {
                    throw new ClassFormatError();
                } else {
                    if (var2) {
                        this.resolveClass(var3);
                    }

                    return var3;
                }
            }
        }
    }
}
