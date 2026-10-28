/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sparrow.spring.filter.monitor;

import com.sparrow.protocol.POJO;
import com.sparrow.protocol.enums.DeviceType;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class MonitorResult implements POJO {
    /**
     * 用linked hash map 会有顺序问题
     */
    private List<MonitorPair> accessCountPerIp;

    /**
     * key:毫秒时间戮
     * value：请求数
     */
    private List<MonitorPair> qps;

    /**
     * 每天的请求数，近一周
     */
    private List<MonitorPair> accessCountPerDay;

    /**
     * 总的IP数
     */
    private Long ipCount;

    /**
     * 总的访问数
     */
    private Long accessCount;

    /**
     * 当前在线用户列表
     * key:用户id
     * value：channel info
     */
    private Map<String, Map<DeviceType, String>> onlineUserMap;
}
