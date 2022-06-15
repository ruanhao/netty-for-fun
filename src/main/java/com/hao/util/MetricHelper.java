package com.hao.util;

import io.netty.util.Version;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MetricHelper {

    public static Set<Map.Entry<String, Version>> version() {
        return Version.identify().entrySet();
    }
}
