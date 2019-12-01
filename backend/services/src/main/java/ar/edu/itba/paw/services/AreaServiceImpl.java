package ar.edu.itba.paw.services;


import ar.edu.itba.paw.interfaces.persistence.AreaDao;
import ar.edu.itba.paw.interfaces.service.AreaService;
import ar.edu.itba.paw.models.Area;
import ar.edu.itba.paw.models.PagedResults;
import ar.edu.itba.paw.services.utils.PaginationResultBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AreaServiceImpl implements AreaService {

    private static final int PAGE_SIZE = 3;
    private static final Logger LOGGER = LoggerFactory.getLogger(AreaServiceImpl.class);

    @Autowired
    private AreaDao areaDao;

    @Autowired
    private PaginationResultBuilder pagedResultBuilder;

    @Override
    public Area findAreaById(final long id) {
        LOGGER.debug("Searching for area with id {}", id);
        return areaDao.findById(id);
    }

    @Override
    public Area create(final String name, final String description, final byte[] image) {
        LOGGER.debug("Creating area with name {}", name);
        return areaDao.create(name,description, image);
    }


    @Override
    public PagedResults<Area> filterAreasByName(final String name, final int page){
        if(page <= 0) {
            LOGGER.error("Attempted to find 0 or negative page number");
            return null;
        }

        LOGGER.debug("Searching for areas with name containing {}", name);
        final List<Area> areas = areaDao.filterAreasByName(name, PAGE_SIZE, PAGE_SIZE * (page - 1));
        final long total = areaDao.totalAreasByName(name);

        return pagedResultBuilder.getPagedResults(areas, total, page, PAGE_SIZE);
    }

}
