import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.imageio.ImageIO;

/**
 * Draws the button backgrounds and 16x16 icons into the GUI sprite folder.
 * Run: java tools/IconGen.java src/main/resources/assets/inventorybuttons/textures/gui/sprites
 *
 * Icons are pixel art without an outline; every transparent pixel next to a filled one becomes the outline
 * colour, so the art stays readable on the blue buttons.
 */
public final class IconGen {
    private static final int OUTLINE = 0xFF1B1E2E;
    private static final Map<Character, Integer> PALETTE = Map.ofEntries(
        Map.entry('w', 0xFFFFFFFF), Map.entry('g', 0xFFC8D0D8), Map.entry('G', 0xFF8A96A3), Map.entry('d', 0xFF5A6270),
        Map.entry('y', 0xFFFFD83A), Map.entry('o', 0xFFF5A524), Map.entry('b', 0xFFB8E6FF), Map.entry('B', 0xFF9FD4FF),
        Map.entry('r', 0xFFE03C3C), Map.entry('n', 0xFF5BBF3A), Map.entry('N', 0xFF3A8A26), Map.entry('m', 0xFF8A5A32),
        Map.entry('M', 0xFF5E3B1F), Map.entry('s', 0xFFEDEDED), Map.entry('S', 0xFFA0A0A0), Map.entry('h', 0xFF7A4E2A),
        Map.entry('p', 0xFFF2E3B3), Map.entry('P', 0xFFB89A5A), Map.entry('e', 0xFF35C7A8), Map.entry('E', 0xFF1C7D69),
        Map.entry('c', 0xFFE8C985), Map.entry('C', 0xFFB8914A), Map.entry('v', 0xFF7B3FB8)
    );

    private static final String[] CLOUD = {
        "................",
        "................",
        ".....wwww.......",
        "....wwwwwww.....",
        "..wwwwwwwwwww...",
        ".wwwwwwwwwwwwww.",
        ".wwwwwwwwwwwwww.",
        "..gggggggggggg..",
    };

    private static final String[] DARK_CLOUD = {
        "................",
        "................",
        ".....gggg.......",
        "....gggggGG.....",
        "..GggggggGGGG...",
        ".GGGgggGGGGGGGd.",
        ".GGGGGGGGGGGGdd.",
        "..dddddddddddd..",
    };

    public static void main(String[] args) throws IOException {
        File root = new File(args[0]);
        File icons = new File(root, "icon");
        icons.mkdirs();

        write(button(0xFF7CCBFF, 0xFF2A7FD4, 0xFF0D2A4A, 0), new File(root, "button.png"));
        write(button(0xFFA6DDFF, 0xFF4A9CEB, 0xFFFFFFFF, 0), new File(root, "button_hovered.png"));
        write(button(0xFF7CCBFF, 0xFF2A7FD4, 0xFF0D2A4A, 0xFFFFCC33), new File(root, "button_selected.png"));
        write(button(0xFF9AA3AD, 0xFF5D6670, 0xFF2A2F36, 0), new File(root, "button_disabled.png"));

        Map<String, BufferedImage> art = new LinkedHashMap<>();
        art.put("survival", grid(
            "................",
            "................",
            "...........sss..",
            "..........ssSs..",
            ".........ssSs...",
            "........ssSs....",
            ".......ssSs.....",
            "..S...ssSs......",
            "..SS.ssSs.......",
            "...SSsSs........",
            "....SSs.........",
            "...hSSSS........",
            "..hh..SS........",
            ".hh.............",
            ".h..............",
            "................"));
        art.put("creative", grid(
            "................",
            "................",
            "..nnnNnnnnNnnn..",
            "..nNnnnnNnnnNn..",
            "..NnnnNnnnnNnn..",
            "..mNmmmNmmNmmN..",
            "..mmmmmmmmmmmm..",
            "..mMmmmmmMmmmm..",
            "..mmmmMmmmmmMm..",
            "..mmmmmmmmmmmm..",
            "..mMmmmmMmmmmm..",
            "..mmmmmmmmmMmm..",
            "..mmmMmmmmmmmm..",
            "..mmmmmmmmMmmm..",
            "................",
            "................"));
        art.put("adventure", grid(
            "................",
            "................",
            "..pppppppppppP..",
            "..pnnppppbbppP..",
            "..pnnnpppbbbpP..",
            "..ppnpppppbbpP..",
            "..pPpppppppppP..",
            "..ppPppppppppP..",
            "..pppPPpprprpP..",
            "..pppppPpprppP..",
            "..pbbppppPrprP..",
            "..pbbbppppppnP..",
            "..ppbpppppnnnP..",
            "..PPPPPPPPPPPP..",
            "................",
            "................"));
        art.put("spectator", grid(
            "................",
            "................",
            "................",
            "................",
            "......EEEE......",
            "....EeeeeeeE....",
            "...Eeee..eeeE...",
            "..Eeee.ww.eeeE..",
            "..Eeee.ww.eeeE..",
            "...Eeee..eeeE...",
            "....EeeeeeeE....",
            "......EEEE......",
            "................",
            "................",
            "................",
            "................"));
        art.put("day", sunrise());
        art.put("night", moon());
        art.put("clear", sun());
        art.put("rain", withCloud(CLOUD,
            "................",
            "...b....b....b..",
            "...b....b....b..",
            "......b....b....",
            "......b....b....",
            "..b.......b.....",
            "..b.......b.....",
            "................"));
        art.put("thunder", withCloud(DARK_CLOUD,
            "........yy......",
            ".......yy.......",
            "..b...yyyy...b..",
            "..b.....yy...b..",
            "........y.......",
            ".......y........",
            "................",
            "................"));
        art.put("snow", grid(
            "................",
            ".......w........",
            ".....w.w.w......",
            "......www.......",
            "..w....w....w...",
            "...w...w...w....",
            "....w..w..w.....",
            ".wwwwwwwwwwwww..",
            "....w..w..w.....",
            "...w...w...w....",
            "..w....w....w...",
            "......www.......",
            ".....w.w.w......",
            ".......w........",
            "................",
            "................"));
        art.put("snowstorm", withCloud(DARK_CLOUD,
            "................",
            "..w......w......",
            ".www....www..w..",
            "..w..w...w..www.",
            "....www......w..",
            ".....w...w......",
            "........www.....",
            ".........w......"));
        art.put("hail", withCloud(CLOUD,
            "................",
            "................",
            "..ww....ww......",
            "..wg....wg......",
            ".....ww.....ww..",
            ".....wg.....wg..",
            "..ww............",
            "..wg............"));
        art.put("fog", grid(
            "................",
            "................",
            "................",
            "....gggggggg....",
            "................",
            "..wwwwwwwwwww...",
            "................",
            "...gggggggggggg.",
            "................",
            ".wwwwwwwwwwww...",
            "................",
            "...gggggggggg...",
            "................",
            "................",
            "................",
            "................"));
        art.put("sandstorm", grid(
            "................",
            "................",
            "...........c....",
            "..ccccccccc.c...",
            "............c...",
            ".CCCCCCCCCCC....",
            "........c.......",
            "...cccccccccc...",
            "..............c.",
            ".CCCCCCCC..CCc..",
            "...........C....",
            "..cccccccccc....",
            "..........c.....",
            "........cc......",
            "................",
            "................"));
        art.put("natural", grid(
            "................",
            "................",
            ".....wwwww.w....",
            "...www...wwww...",
            "..ww......www...",
            "..w......wwww...",
            ".ww.............",
            ".w............w.",
            ".w............w.",
            ".............ww.",
            "...wwww......w..",
            "...www......ww..",
            "...wwww...www...",
            "...w.wwwww......",
            "................",
            "................"));

        // GS Mod: daily reward (gift box) and daily missions (clipboard)
        art.put("daily", grid(
            "................",
            "....yy....yy....",
            "...y..y..y..y...",
            "....yyyyyyyy....",
            "..rrrrryyrrrrr..",
            "..rrrrryyrrrrr..",
            "..oooooyyooooo..",
            "...rrrryyrrrr...",
            "...rrrryyrrrr...",
            "...rrrryyrrrr...",
            "...rrrryyrrrr...",
            "...rrrryyrrrr...",
            "...rrrryyrrrr...",
            "...oooooooooo...",
            "................",
            "................"));
        art.put("missions", grid(
            "................",
            "......dddd......",
            "...hhhdggdhhh...",
            "...hpppppppph...",
            "...hpnpPPPPph...",
            "...hnnpppppph...",
            "...hpppppppph...",
            "...hpnpPPPPph...",
            "...hnnpppppph...",
            "...hpppppppph...",
            "...hpPpPPPPph...",
            "...hpppppppph...",
            "...hpppppppph...",
            "...hhhhhhhhhh...",
            "................",
            "................"));

        for (Map.Entry<String, BufferedImage> entry : art.entrySet()) {
            write(outline(entry.getValue()), new File(icons, entry.getKey() + ".png"));
        }
    }

    /** A round 20x20 button: vertical gradient, 1px border, optional inner ring for the selected state. */
    private static BufferedImage button(int top, int bottom, int border, int ring) {
        BufferedImage image = new BufferedImage(20, 20, BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < 20; y++) {
            for (int x = 0; x < 20; x++) {
                double d = Math.hypot(x - 9.5, y - 9.5);
                if (d > 9.9) {
                    continue;
                }
                int color;
                if (d > 8.7) {
                    color = border;
                } else if (ring != 0 && d > 7.6) {
                    color = ring;
                } else {
                    color = lerp(top, bottom, y / 19.0);
                    // soft highlight on the upper left rim
                    if (d > 6.8 && x + y < 16) {
                        color = lerp(color, 0xFFFFFFFF, 0.35);
                    }
                }
                image.setRGB(x, y, color);
            }
        }
        return image;
    }

    private static BufferedImage sun() {
        BufferedImage image = blank();
        for (int y = 0; y < 16; y++) {
            for (int x = 0; x < 16; x++) {
                double dx = x - 7.5;
                double dy = y - 7.5;
                double d = Math.hypot(dx, dy);
                if (d <= 3.6) {
                    image.setRGB(x, y, dx + dy > 2.5 ? PALETTE.get('o') : PALETTE.get('y'));
                }
            }
        }
        // eight rays
        int[][] rays = {{7, 1}, {8, 1}, {7, 2}, {8, 2}, {7, 13}, {8, 13}, {7, 14}, {8, 14}, {1, 7}, {1, 8}, {2, 7}, {2, 8},
            {13, 7}, {13, 8}, {14, 7}, {14, 8}, {3, 3}, {12, 3}, {3, 12}, {12, 12}};
        for (int[] p : rays) {
            image.setRGB(p[0], p[1], PALETTE.get('y'));
        }
        return image;
    }

    private static BufferedImage sunrise() {
        BufferedImage image = blank();
        for (int y = 0; y < 16; y++) {
            for (int x = 0; x < 16; x++) {
                double d = Math.hypot(x - 7.5, y - 10.5);
                if (y <= 9 && d <= 4.6) {
                    image.setRGB(x, y, y >= 8 ? PALETTE.get('o') : PALETTE.get('y'));
                }
            }
        }
        int[][] rays = {{7, 2}, {8, 2}, {7, 3}, {8, 3}, {2, 4}, {3, 5}, {13, 4}, {12, 5}, {1, 8}, {14, 8}};
        for (int[] p : rays) {
            image.setRGB(p[0], p[1], PALETTE.get('y'));
        }
        for (int x = 1; x <= 14; x++) {
            image.setRGB(x, 11, PALETTE.get('n'));
            image.setRGB(x, 12, (x % 4 == 0) ? PALETTE.get('n') : PALETTE.get('N'));
        }
        return image;
    }

    private static BufferedImage moon() {
        BufferedImage image = blank();
        for (int y = 0; y < 16; y++) {
            for (int x = 0; x < 16; x++) {
                double outer = Math.hypot(x - 7.0, y - 8.0);
                double inner = Math.hypot(x - 10.0, y - 6.0);
                if (outer <= 5.8 && inner > 4.6) {
                    image.setRGB(x, y, outer > 4.6 ? PALETTE.get('g') : PALETTE.get('w'));
                }
            }
        }
        int[][] stars = {{12, 2}, {13, 9}, {11, 12}};
        for (int[] p : stars) {
            image.setRGB(p[0], p[1], PALETTE.get('y'));
        }
        return image;
    }

    private static BufferedImage withCloud(String[] cloud, String... below) {
        String[] rows = new String[16];
        System.arraycopy(cloud, 0, rows, 0, 8);
        System.arraycopy(below, 0, rows, 8, 8);
        return grid(rows);
    }

    private static BufferedImage grid(String... rows) {
        if (rows.length != 16) {
            throw new IllegalArgumentException("icon needs 16 rows, got " + rows.length);
        }
        BufferedImage image = blank();
        for (int y = 0; y < 16; y++) {
            if (rows[y].length() != 16) {
                throw new IllegalArgumentException("row " + y + " is " + rows[y].length() + " wide: " + rows[y]);
            }
            for (int x = 0; x < 16; x++) {
                char c = rows[y].charAt(x);
                if (c != '.') {
                    Integer color = PALETTE.get(c);
                    if (color == null) {
                        throw new IllegalArgumentException("unknown colour '" + c + "'");
                    }
                    image.setRGB(x, y, color);
                }
            }
        }
        return image;
    }

    private static BufferedImage outline(BufferedImage art) {
        BufferedImage out = blank();
        for (int y = 0; y < 16; y++) {
            for (int x = 0; x < 16; x++) {
                int argb = art.getRGB(x, y);
                if ((argb >>> 24) != 0) {
                    out.setRGB(x, y, argb);
                } else if (filled(art, x - 1, y) || filled(art, x + 1, y) || filled(art, x, y - 1) || filled(art, x, y + 1)) {
                    out.setRGB(x, y, OUTLINE);
                }
            }
        }
        return out;
    }

    private static boolean filled(BufferedImage image, int x, int y) {
        return x >= 0 && y >= 0 && x < 16 && y < 16 && (image.getRGB(x, y) >>> 24) != 0;
    }

    private static BufferedImage blank() {
        return new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
    }

    private static int lerp(int a, int b, double t) {
        int r = (int) Math.round(((a >> 16) & 0xFF) * (1 - t) + ((b >> 16) & 0xFF) * t);
        int g = (int) Math.round(((a >> 8) & 0xFF) * (1 - t) + ((b >> 8) & 0xFF) * t);
        int bl = (int) Math.round((a & 0xFF) * (1 - t) + (b & 0xFF) * t);
        return 0xFF000000 | (r << 16) | (g << 8) | bl;
    }

    private static void write(BufferedImage image, File file) throws IOException {
        ImageIO.write(image, "png", file);
    }
}
