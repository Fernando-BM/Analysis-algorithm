package mx.unam.ciencias.edd;

/**
 * Clase para fabricar dispersores.
 */
public class FabricaDispersores {

    /**
     * Regresa una instancia de {@link Dispersor} para cadenas.
     * @param algoritmo el algoritmo de dispersor que se desea.
     * @return una instancia de {@link Dispersor} para cadenas.
     * @throws IllegalArgumentException si recibe un identificador no
     *         reconocido.
     */
    public static Dispersor<String> getInstancia(AlgoritmoDispersor algoritmo) {
        
	switch (algoritmo) {
        case BJ_STRING:   
	return getBJString();
        case GLIB_STRING: 
	return getGLibString();
        case XOR_STRING:  
	return getXORString();
        default: throw new IllegalArgumentException();
        }
    }

    private static int ss(int a, int b) {
        long la = a;
        long lb = b;
        long r = (la + lb) & 0xffffffff;
        return (int)r;
    }


    private static byte ss(byte a, byte b) {
        int ia = a;
        int ib = b;
        int r = (ia + ib) & 0xff;
        return (byte)r;
    }


    private static int ms(int a, int b) {
        long la = a;
        long lb = b;
        long r = (la * lb) & 0xffffffff;
        return (int)r;
    }


    private static void mezclaBJ(int[] r) {
        int a = r[0], b = r[1], c = r[2];

        a = ss(a, -b); a = ss(a, -c); a ^= (c >>> 13);
        b = ss(b, -c); b = ss(b, -a); b ^= (a << 8);
        c = ss(c, -a); c = ss(c, -b); c ^= (b >>> 13);
        a = ss(a, -b); a = ss(a, -c); a ^= (c >>> 12);
        b = ss(b, -c); b = ss(b, -a); b ^= (a << 16);
        c = ss(c, -a); c = ss(c, -b); c ^= (b >>> 5);
        a = ss(a, -b); a = ss(a, -c); a ^= (c >>> 3);
        b = ss(b, -c); b = ss(b, -a); b ^= (a << 10);
        c = ss(c, -a); c = ss(c, -b); c ^= (b >>> 15);
        r[0] = a; r[1] = b; r[2] = c;
    }

 
    private static Dispersor<String> getBJString() {
        return (cadena) -> {
            byte[] k = cadena.getBytes();
            int n = k.length;

            int[] r = { 0x9e3779b9, 0x9e3779b9, 0xffffffff };

            int i = 0;
            while (i+11 < n) {
                r[0] = ss(r[0], k[i] + (k[i+1] <<  8) + (k[i+2] << 16) + (k[i+3] << 24));
                r[1] = ss(r[1], k[i+4] + (k[i+5] <<  8) + (k[i+6] << 16) + (k[i+7] << 24));
                r[2] = ss(r[2], k[i+8] + (k[i+9] << 8) + (k[i+10] << 16) + (k[i+11] << 24));
                mezclaBJ(r);
                i += 12;
            }

            r[2] += n;
            switch (n - i) {
            case 11: r[2] = ss(r[2], k[i+10] << 24);
            case 10: r[2] = ss(r[2], k[i+9] << 16);
            case  9: r[2] = ss(r[2], k[i+8] <<  8);
            case  8: r[1] = ss(r[1], k[i+7] << 24);
            case  7: r[1] = ss(r[1], k[i+6] << 16);
            case  6: r[1] = ss(r[1], k[i+5] <<  8);
            case  5: r[1] = ss(r[1], k[i+4]);
            case  4: r[0] = ss(r[0], k[i+3] << 24);
            case  3: r[0] = ss(r[0], k[i+2] << 16);
            case  2: r[0] = ss(r[0], k[i+1] <<  8);
            case  1: r[0] = ss(r[0], k[i]);
            }

            mezclaBJ(r);
            return r[2];
        };
    }


    private static Dispersor<String> getGLibString() {
        return (cadena) -> {
            byte[] k = cadena.getBytes();
            int h = 5381;
            for (int i = 0; i < k.length; i++)
                h = ss(ms(h, 33), k[i]);
            return h;
        };
    }

    
    private static Dispersor<String> getXORString() {
        return (cadena) -> {
            byte[] k = cadena.getBytes();
            int l = k.length;
            int r = 0, i = 0;
            while (l >= 4) {
                r ^= (k[i] << 24) | (k[i+1] << 16) | (k[i+2] << 8) | k[i+3];
                i += 4;
                l -= 4;
            }
            int t = 0;
            switch (l) {
            case 3: t |= k[i+2] << 8;
            case 2: t |= k[i+1] << 16;
            case 1: t |= k[i]   << 24;
            }
            r ^= t;
            return r;
        };
    }
}
