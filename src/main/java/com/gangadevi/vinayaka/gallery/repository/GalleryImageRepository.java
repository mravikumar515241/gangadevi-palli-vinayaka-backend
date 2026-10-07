package com.gangadevi.vinayaka.gallery.repository;

import com.gangadevi.vinayaka.gallery.entity.GalleryImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GalleryImageRepository extends JpaRepository<GalleryImage, Long> {
    List<GalleryImage> findByFestivalYearYearOrderBySortOrderAscIdAsc(Integer year);

    long countByFestivalYearYear(Integer year);
}
