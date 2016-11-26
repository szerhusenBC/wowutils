package org.zerhusen.wow.tsm.renderer;

import org.zerhusen.wow.tsm.service.TSMPriceCategory;
import org.zerhusen.wow.tsm.service.WowItem;

import java.util.List;
import java.util.Map;

/**
 * Created by stephan on 19.11.16.
 */
public interface FileRenderer {

    void render(String rootPath, Map<TSMPriceCategory, List<WowItem>> categoryPriceMap);

}
