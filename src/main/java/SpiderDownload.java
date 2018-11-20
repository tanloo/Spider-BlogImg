import mapper.ImageMapper;
import org.apache.ibatis.session.SqlSession;
import pojo.Image;
import utils.SqlSessionFactoryUtils;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;

/**
 * Spider data persistence
 *
 * @author tanloo
 * @date 2018/11/19
 */
class SpiderDownload {
    /**
     * @param image ImagePOJO
     */
    static void save2DB(Image image) {
        SqlSession sqlSession = null;
        try {
            sqlSession = SqlSessionFactoryUtils.openSqlSession();
            ImageMapper roleMapper = sqlSession.getMapper(ImageMapper.class);
            roleMapper.insertImage(image);
            sqlSession.commit();
        } catch (Exception e) {
            e.printStackTrace();
            sqlSession.rollback();
        } finally {
            if (sqlSession != null) {
                sqlSession.close();
            }
        }
    }

    /**
     * @param urlStr 图片URL
     * @param filename 图片文件名
     * @param savePath  存储路径
     * @throws IOException 读写异常
     */
    static void download(String urlStr, String filename, String savePath) throws IOException {

        URL url = new URL(urlStr);
        //设置本地SSR代理
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 1080));
        //打开url连接
        URLConnection connection = url.openConnection(proxy);
        //请求超时时间
        connection.setConnectTimeout(5000);
        //输入流
        InputStream in = connection.getInputStream();
        //缓冲数据
        byte[] bytes = new byte[2048];
        //数据长度
        int len;
        //文件
        File file = new File(savePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        OutputStream out = new FileOutputStream(file.getPath() + "\\" + filename);
        //先读到bytes中
        while ((len = in.read(bytes)) != -1) {
            //再从bytes中写入文件
            out.write(bytes, 0, len);
        }
        //关闭IO
        out.close();
        in.close();
    }

}
