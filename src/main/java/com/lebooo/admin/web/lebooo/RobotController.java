package com.lebooo.admin.web.lebooo;

import com.google.common.collect.Lists;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletRequest;
import java.util.Iterator;
import java.util.List;

/**
 * @author: Wei Liu
 * Date: 13-5-23
 * Time: PM2:55
 */
@Controller
@RequestMapping(value = "/lebooo/robot")
public class RobotController {
    @RequestMapping(method = RequestMethod.GET)
    public String listPage(@RequestParam(value = "sortType", defaultValue = "auto") String sortType,
                           @RequestParam(value = "page", defaultValue = "1") int pageNumber, Model model, ServletRequest request) {
        model.addAttribute("robots", new Page() {
            @Override
            public int getNumber() {
                return 0;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public int getSize() {
                return 0;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public int getTotalPages() {
                return 0;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public int getNumberOfElements() {
                return 0;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public long getTotalElements() {
                return 0;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public boolean hasPreviousPage() {
                return false;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public boolean isFirstPage() {
                return false;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public boolean hasNextPage() {
                return false;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public boolean isLastPage() {
                return false;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public Iterator iterator() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public List getContent() {
                return Lists.newArrayList();  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public boolean hasContent() {
                return false;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public Sort getSort() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }
        });

        return "lebooo/robotUserList";
    }
}
