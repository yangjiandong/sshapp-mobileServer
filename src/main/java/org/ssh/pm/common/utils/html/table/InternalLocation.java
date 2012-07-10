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

/**
 * An enum constant for internal locations of a table. This can be used to
 * identify whether operations on the table should apply to rows and / or
 * columns.
 */

public enum InternalLocation implements Location {

  /**
   * This location relates to all rows of the table
   */

  ROW,

  /**
   * This location relates to all columns of the table
   */

  COLUMN;
}