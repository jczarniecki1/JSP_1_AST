package edu.pjwstk.demo.datastore;

import edu.pjwstk.jps.datastore.IOID;

public final class StaticReferenceResolver {

    public static String valueOrName(IOID objectId) {
         return StoreRepository.getInstance().printById(objectId);
    }
}
