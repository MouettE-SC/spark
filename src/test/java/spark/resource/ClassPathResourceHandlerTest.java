package spark.resource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.net.MalformedURLException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;


@RunWith(PowerMockRunner.class)
@PrepareForTest({ClassPathResourceHandler.class, ClassPathResource.class})
class ClassPathResourceHandlerTest {

    private ClassPathResource resourceMock;
    private File fileMock;

    public ClassPathResourceHandlerTest() {
    }

    @Before
    public void setUp() {
        this.resourceMock = PowerMockito.mock(ClassPathResource.class);
        this.fileMock = mock(File.class);
    }

    @Test(expected = MalformedURLException.class)
    public void testGetResource_whenPathIsNull_thenThrowMalformedURLException() throws MalformedURLException {
        String path = null;
        ClassPathResourceHandler classPathResourceHandler = new ClassPathResourceHandler("/public");
        classPathResourceHandler.getResource(path);
    }

    @Test(expected = MalformedURLException.class)
    public void testGetResource_whenPathDoesNotStartWithSlash_thenThrowMalformedURLException() throws
                                                                                               MalformedURLException {
        String path = "folder";
        ClassPathResourceHandler classPathResourceHandler = new ClassPathResourceHandler("/public");
        classPathResourceHandler.getResource(path);
    }

    @Test
    public void testGetResource_whenResourcePathDoesNotExists_thenReturnNull() throws Exception {
        ClassPathResourceHandler classPathResourceHandler = new ClassPathResourceHandler("/public");

        //when
        PowerMockito.whenNew(ClassPathResource.class).withArguments("/public/folder").thenReturn(resourceMock);
        doReturn(false).when(resourceMock).exists();

        //then
        assertNull("Should return null because the resource path doesn't exists", classPathResourceHandler.getResource("/folder"));
        PowerMockito.verifyNew(ClassPathResource.class).withArguments("/public/folder");
        verify(resourceMock).exists();

    }

    @Test
    public void testGetResource_whenResourcePathExists_andResourcePathIsNotADirectory_thenReturnResourcePathObject() throws
                                                                                                                     Exception {
        ClassPathResourceHandler classPathResourceHandler = new ClassPathResourceHandler("/public");

        //when
        PowerMockito.whenNew(ClassPathResource.class).withArguments("/public/index.html").thenReturn(resourceMock);
        doReturn(true).doReturn(true).when(resourceMock).exists();
        doReturn(fileMock).when(resourceMock).getFile();
        doReturn(false).when(fileMock).isDirectory();
        PowerMockito.doReturn("/public/index.html").when(resourceMock).getPath();

        //then

        String returnedPath = ((ClassPathResource) classPathResourceHandler.getResource("/index.html")).getPath();
        assertEquals("Should be equals because the resource exists", returnedPath, "/public/index.html");
        PowerMockito.verifyNew(ClassPathResource.class).withArguments("/public/index.html");
        verify(resourceMock).exists();
    }
}
