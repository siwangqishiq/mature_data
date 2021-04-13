package xyz.panyi.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.panyi.model.Mature;
import xyz.panyi.model.Resp;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

@RestController
public class MatureController {
    public final static Logger logger = LoggerFactory.getLogger(MatureController.class);
    public static List<Mature> matureList;

    public static final int COUNT = 9;
    /**
     * 查询打包历史记录
     * @return
     */
    @GetMapping(value = "matures")
    public Resp<List<Mature>> findImagesBySid(){
        logger.info("/matures");

        List<Mature> list = new ArrayList<Mature>(COUNT);

        int limit = matureList.size();
        HashSet<Integer> set = new HashSet<Integer>();
        Random random = new Random();
        while(set.size() < COUNT){
            int val = random.nextInt(limit);
            set.add(new Integer(val));
        }//end while
        for(int val : set){
            list.add(matureList.get(val));
        }
        logger.info("/matures result size = " + (list!=null?list.size():null));

        return Resp.genResp(list);
    }

}//end class
