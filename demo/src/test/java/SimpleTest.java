import org.junit.Test;
import static org.junit.Assert.*;

/**
 * シンプルなテストクラス
 */
public class SimpleTest {

    @Test
    public void testBasic() {
        // 基本的なテスト
        assertTrue("基本的なテストが通るはず", true);
        assertEquals("1 + 1 = 2", 2, 1 + 1);
    }

    @Test
    public void testString() {
        String message = "Hello World";
        assertNotNull("メッセージはnullではない", message);
        assertEquals("メッセージの長さ", 11, message.length());
    }
}