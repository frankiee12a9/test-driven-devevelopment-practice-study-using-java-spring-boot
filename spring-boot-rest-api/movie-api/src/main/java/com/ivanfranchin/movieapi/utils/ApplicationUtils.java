package com.ivanfranchin.movieapi.utils;

import com.ivanfranchin.movieapi.exception.BadRequestException;

public class ApplicationUtils {

    public static void validatePageNumberAndSize(int page, int size) {
		if (page < 0) {
			throw new BadRequestException("Page number cannot be less than zero.");
		}

		if (size < 0) {
			throw new BadRequestException("Size number cannot be less than zero.");
		}

		if (size > ApplicationConstants.MAX_PAGE_SIZE) {
			throw new BadRequestException("Page size must not be greater than " + ApplicationConstants.MAX_PAGE_SIZE);
		}
	}
}
