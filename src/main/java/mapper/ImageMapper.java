package mapper;

import pojo.Image;

/**
 * @author tanloo
 * @date  2018/11/19
 */
public interface ImageMapper {
    /**
     * insert imagePOJO to DB
     *
     * @param image imagePOJO
     * @return int
     */
    int insertImage(Image image);
}
