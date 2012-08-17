package org.ssh.pm.common.mapping.other;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.adapters.XmlAdapter;


public class MapAdapter extends XmlAdapter<MapElements[], Map<String, AccountBean>> {
    public MapElements[] marshal(Map<String, AccountBean> arg0) throws Exception {
        MapElements[] mapElements = new MapElements[arg0.size()];

        int i = 0;
        for (Map.Entry<String, AccountBean> entry : arg0.entrySet())
            mapElements[i++] = new MapElements(entry.getKey(), entry.getValue());

        return mapElements;
    }

    public Map<String, AccountBean> unmarshal(MapElements[] arg0) throws Exception {
        Map<String, AccountBean> r = new HashMap<String, AccountBean>();
        for (MapElements mapelement : arg0)
            r.put(mapelement.key, mapelement.value);
        return r;
    }
}
