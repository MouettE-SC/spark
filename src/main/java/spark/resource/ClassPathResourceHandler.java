//
//  ========================================================================
//  Copyright (c) 1995-2013 Mort Bay Consulting Pty. Ltd.
//  ------------------------------------------------------------------------
//  All rights reserved. This program and the accompanying materials
//  are made available under the terms of the Eclipse Public License v1.0
//  and Apache License v2.0 which accompanies this distribution.
//
//      The Eclipse Public License is available at
//      http://www.eclipse.org/legal/epl-v10.html
//
//      The Apache License v2.0 is available at
//      http://www.opensource.org/licenses/apache2.0.php
//
//  You may elect to redistribute this code under either of these licenses.
//  ========================================================================
//
package spark.resource;

import java.net.MalformedURLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spark.utils.Assert;

/**
 * Locates resources in classpath
 * Code snippets copied from Eclipse Jetty source. Modifications made by Per Wendel.
 */
public class ClassPathResourceHandler extends AbstractResourceHandler {
    private static final Logger LOG = LoggerFactory.getLogger(ClassPathResourceHandler.class);

    private final String baseResource;

    /**
     * Constructor
     *
     * @param baseResource the base resource path
     */
    public ClassPathResourceHandler(String baseResource) {
        Assert.notNull(baseResource);

        this.baseResource = baseResource;
    }

    @Override
    protected AbstractFileResolvingResource getResource(String path) throws MalformedURLException {

        // Handle root without '/'
       if (path != null && path.length() == 0)
           return null;

        if (path == null || !path.startsWith("/")) {
            throw new MalformedURLException(path);
        }

        try {
            path = UriPath.canonical(path);

            final String addedPath = addPaths(baseResource, path);

            ClassPathResource resource = new ClassPathResource(addedPath);

            return (resource != null && resource.exists()) ? resource : null;
        } catch (Exception e) {
            if (LOG.isDebugEnabled()) {
                LOG.debug(e.getClass().getSimpleName() + " when trying to get resource. " + e.getMessage());
            }
        }
        return null;
    }

}
