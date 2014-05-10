package resourcing;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class VFSImplTest {
    private VFS vfs;
    private static final String testFile = "mysql.xml";
    private static final String startDir = "./configs";

    @Before
    public void setUp() throws Exception {
        vfs = new VFSImpl(startDir);
    }

    @Test
    public void testIsExist() throws Exception {
        assertTrue(vfs.isExist(testFile));
        assertFalse(vfs.isExist("nonexistedfilename.xml"));
    }

    @Test
    public void testIsDirectory() throws Exception {
        assertFalse(vfs.isDirectory(testFile));
        assertTrue(vfs.isDirectory("./"));
    }

    @Test
    public void testGetAbsolutePath() throws Exception {
        String absPath = Paths.get(startDir, testFile).toAbsolutePath().toString();
        assertTrue(vfs.getAbsolutePath(testFile).equals(absPath));
    }

    @Test
    public void getFileName() throws Exception {
        assertTrue(vfs.getFileName(vfs.getAbsolutePath(testFile))
                .equals(testFile));
    }

    @Test
    public void testGetBytes() throws Exception {
        assertTrue(vfs.getBytes(testFile).length != 0);
    }

    private byte[] filterLineBreaks(byte[] arr) {
        //этого метода могло бы не быть
        //если бы Java позволяла получить stream от массива byte[]

        List<Byte> resultList = new ArrayList<Byte>();

        for (byte b: arr) {
            if (b != '\n' && b != '\r')
                resultList.add(b);
        }

        return ArrayUtils.toPrimitive(
                resultList.toArray(new Byte[resultList.size()]));
    }

    @Test
    public void testGetUTF8Text() throws Exception {
        byte[] arr1 = vfs.getUTF8Text(testFile).getBytes("UTF-8");
        byte[] arr2 = filterLineBreaks(vfs.getBytes(testFile));

        assertTrue(Arrays.equals(arr1, arr2));
    }

    @Test
    public void testGetIterator() throws Exception {
        Stream<String> stream = StreamSupport.stream(vfs.iterate(startDir).spliterator(), false);

        assertTrue(stream.anyMatch(fileName -> fileName.contains(testFile)));
    }
}