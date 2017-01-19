package com.eprotech.rates.server.properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 *
 * Created by mercury on 20-Aug-16.
 */
public class CcyPairs {
    private static final Logger logger = LoggerFactory.getLogger(CcyPairs.class);
    private static final CcyPairs ccyPairs = new CcyPairs();
    private static final Lock lock = new ReentrantLock();
    public static final String CCY_PAIRS_TXT_FILENAME = "/ccy_pairs.txt";
    private List<String> ccyPairsList;

    private CcyPairs() {
    }

    public static CcyPairs getInstance() {
        return ccyPairs;
    }

    public List<String> getCcyPairs() throws Exception {
        logger.info("getCcyPairs:");
        lock.lock();
        try {
            if (ccyPairsList == null) {
                logger.info("ref is null");
                ccyPairsList = loadCcyPairs();
            }
            return new CopyOnWriteArrayList<>(ccyPairsList);
        } finally {
            lock.unlock();
        }
    }

    private List<String> loadCcyPairs() throws Exception {
        List<String> ccyPairs = null;
        try {
            // the slash makes it absolute from classpath, otherwise relative from class's location
            String pathnameWithSlash = CCY_PAIRS_TXT_FILENAME;

            /*File f = new File(pathname);
            System.out.println(f.getAbsolutePath() + " - " + f.exists());
            System.out.println(Main.class.getResourceAsStream(pathnameWithSlash));
            System.out.println(Main.class.getResourceAsStream(pathname));
            System.out.println(Main.class.getResource(pathnameWithSlash));*/

            URI uri = CcyPairs.class.getResource(pathnameWithSlash).toURI();
            logger.debug("URI:- {} ,uri schema:- {}", uri, uri.getScheme());

            Path path;

            if (uri.getScheme().equals("jar")) {
                // Jar should be treated differently when using NIO, or ideally use getResourceAsStream
                final String[] array = uri.toString().split("!");
                final Map<String, String> env = new HashMap<>();
                final FileSystem fs = FileSystems.newFileSystem(URI.create(array[0]), env);

                path = fs.getPath(array[1]);

                /*
                FileSystemProvider.installedProviders().stream().filter(provider -> provider.getScheme().equalsIgnoreCase(uri.getScheme())).forEach(provider -> {
                System.out.println("Provider.getPath:- " + provider.getPath(uri) + " ," + provider.toString() + " , fileSystem" + provider.getFileSystem(uri));
                System.out.println();
                });
                */
            } else {
                path = Paths.get(uri);
            }

            ccyPairs = Files.readAllLines(path, Charset.forName("UTF8"));
            ccyPairs = ccyPairs.stream().map(s -> s = s.substring(0, 7)).collect(Collectors.toList());

            /*List<String> str2 = new ArrayList<>();
            for (String ccyPair : ccyPairs) {
                str2.add(ccyPair.substring(0,7));
                System.out.println(str2);
            }
            ccyPairs = str2;*/
            logger.info(String.valueOf(ccyPairs));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Failed loading property file.");
            throw e;
        }
        return ccyPairs;
    }
}
