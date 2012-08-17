/**
 * Copyright 2007 Dr. Matthias Laux
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ssh.pm.common.utils.html.table;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * This class represents cells in the table. Cells can span more than one row and column.
 * Instances of this class are also used to hold all data pertaining to a cell and thus serves
 * as a vehicle to transport data into a Velocity template.
 */

public class Cell {

    private String name = null;
    private Map<String, String> properties = null; // For HTML formatting properties
    private int rowSpan = 1;
    private int colSpan = 1;
    private Map<String, Object> content = new HashMap<String, Object>();
    private Set<String> types = new TreeSet<String>();

    private String urlName = null;
    private String urlName2 = null;
    private String align = "left";
    private int width = 14;

    /**
     * Constructor for a simple cell with 1 row and 1 column.
     *
     * @param name The name given to the cell. This can be used as a descriptive text when
     *             necessary
     */

    public Cell(String name) {
        this(name, 1, 1);
    }

    /**
     * Constructor for a cell.
     *
     * @param name    The name given to the cell. This can be used as a descriptive text when
     *                necessary
     * @param rowSpan The number of rows that this cell spans
     * @param colSpan The number of columns that this cell spans
     */

    public Cell(String name, int rowSpan, int colSpan) {
        this(new HashMap<String, String>(), name, rowSpan, colSpan);
    }

    /**
     * Constructor for a cell.
     *
     * @param properties The set of properties for this cell
     * @param name       The name given to the cell. This can be used as a descriptive text when
     *                   necessary
     * @param rowSpan    The number of rows that this cell spans
     * @param colSpan    The number of columns that this cell spans
     */

    public Cell(Map<String, String> properties, String name, int rowSpan, int colSpan) {
        if (properties == null) {
            throw new IllegalArgumentException("properties may not be null");
        }
        if (name == null) {
            throw new IllegalArgumentException("name may not be null");
        }
        if (rowSpan < 1) {
            throw new IllegalArgumentException("rowSpan must be larger than 0");
        }
        if (colSpan < 1) {
            throw new IllegalArgumentException("colSpan must be larger than 0");
        }
        this.setColSpan(colSpan);
        this.setRowSpan(rowSpan);
        this.properties = properties;
        this.name = name;
    }

    /**
     * Create a deep copy of the current cell.
     *
     * @return A deep copy with all properties, types and content elements.
     */

    public Cell clone() {
        Cell clone = new Cell(getName(), getRowSpan(), getColSpan());
        for (String key : getProperties().keySet()) {
            clone.setProperty(key, getProperty(key));
        }
        for (String key : getContent().keySet()) {
            clone.setContent(key, getContent(key));
        }
        for (String type : getTypes()) {
            clone.setType(type);
        }
        return clone;
    }

    /**
     * Retrieve the properties defined for this cell.
     *
     * @return The properties map for this cell
     */

    public Map<String, String> getProperties() {
        return properties;
    }

    /**
     * Retrieve the content elements defined for this cell.
     *
     * @return The content element map for this cell
     */

    public Map<String, Object> getContent() {
        return content;
    }

    /**
     * Retrieve the types defined for this cell.
     *
     * @return The type set for this cell
     */

    public Set<String> getTypes() {
        return types;
    }

    /**
     * Retrieve the name of the cell.
     *
     * @return The name of the cell
     */

    public String getName() {
        return name;
    }

    /**
     * Retrieve the number of rows that this cell spans.
     *
     * @return The number of rows that this cell spans
     */

    public int getRowSpan() {
        return rowSpan;
    }

    /**
     * Retrieve the number of columns that this cell spans.
     *
     * @return The number of columns that this cell spans
     */

    public int getColSpan() {
        return colSpan;
    }

    /**
     * Set a type for this cell. Types are string-valued markers, and any number of types
     * can be attached to a cell using this method. Inside the Velocity template,
     * cells can be checked for types using the {@link #isType(String)} method. This
     * allows the template to handle cells with different types differently (e. g. in the
     * layout).
     *
     * @param type The type to add for this cell
     */

    public void setType(String type) {
        if (type == null) {
            throw new IllegalArgumentException("type may not be null");
        }
        types.add(type);
    }

    /**
     * Check whether a given type is set for this cell. This is useful inside Velocity
     * templates to allow for type-specific handling of cell layout.
     *
     * @param type The type to check for in this cell
     *
     * @return A boolean indicating whether the given type has been set for this cell
     */

    public boolean isType(String type) {
        if (type == null) {
            throw new IllegalArgumentException("type may not be null");
        }
        return types.contains(type);
    }

    /**
     * Retrieve a property value.
     *
     * @param key The key for this peoperty
     *
     * @return The value for the given key
     */

    public String getProperty(String key) {
        if (key == null) {
            throw new IllegalArgumentException("key may not be null");
        }
        if (!properties.containsKey(key)) {
            throw new IllegalArgumentException("Unknown property key: " + key);
        }
        return properties.get(key);
    }

    /**
     * Set a property value. Properties are another means to equip a cell with
     * configuration information or content data, and any number of key/value pairs
     * can be attached to a cell and used in Velocity templates when processing the cell.
     *
     * @param key   The property key
     * @param value The property value
     */

    public void setProperty(String key, String value) {
        if (key == null) {
            throw new IllegalArgumentException("key may not be null");
        }
        if (value == null) {
            throw new IllegalArgumentException("value may not be null");
        }
        properties.put(key, value);
    }

    /**
     * Retrieve the content object associated with the given key.
     *
     * @param key The key identifying the content object
     *
     * @return The content object associated with the given key
     */

    public Object getContent(String key) {
        if (key == null) {
            throw new IllegalArgumentException("key may not be null");
        }
        return content.get(key);
    }

    /**
     * Set a content object. Content objects are used to attach data to a cell
     * which can then be used in the template, for example to attach a picture
     * or a table with the results of a DB query to an HTML cell. The controller
     * program which sets up the table/cell structure would add such content objects
     * to the cells, and the Velocity template would retrieve the data using the
     * keys and add it to the HTML cell structure.
     *
     * @param key   The key by which this content object is identified
     * @param value The actual content object
     */

    public void setContent(String key, Object value) {
        if (key == null) {
            throw new IllegalArgumentException("key may not be null");
        }
        if (value == null) {
            throw new IllegalArgumentException("value may not be null");
        }
        content.put(key, value);
    }

    /**
     * Set the number of rows that this cell spans. The original value set in the
     * constructor my change when cells are clipped during insertion into the table.
     *
     * @see BoundaryCondition
     *
     * @param rowSpan The number of rows that this cell spans
     */

    public void setRowSpan(int rowSpan) {
        if (rowSpan < 1) {
            throw new IllegalArgumentException("rowSpan must be greater than 0");
        }
        this.rowSpan = rowSpan;
    }

    /**
     * Set the number of columns that this cell spans. The original value set in the
     * constructor my change when cells are clipped during insertion into the table.
     *
     * @see BoundaryCondition
     *
     * @param colSpan The number of columns that this cell spans
     */

    public void setColSpan(int colSpan) {
        if (colSpan < 1) {
            throw new IllegalArgumentException("colSpan must be greater than 0");
        }
        this.colSpan = colSpan;
    }

    /**
     * The overridden {@link Object#toString()} method.
     *
     * @return A string representation of the instance with all relevant data
     */

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Cell: name = ");
        sb.append(name);
        sb.append(" / rowSpan = ");
        sb.append(rowSpan);
        sb.append(" / colSpan = ");
        sb.append(colSpan);
        return sb.toString();
    }

    public String getUrlName() {
        return urlName;
    }

    public void setUrlName(String urlName) {
        this.urlName = urlName;
    }

    public String getAlign() {
        return align;
    }

    public void setAlign(String align) {
        this.align = align;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public String getUrlName2() {
        return urlName2;
    }

    public void setUrlName2(String urlName2) {
        this.urlName2 = urlName2;
    }

}
