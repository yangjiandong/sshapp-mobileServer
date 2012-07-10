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
 * An enum constant for the different supported boundary conditions.
 */

public enum BoundaryCondition {

  /**
   * Any cell location outside of the predefined area leads to an exception.
   * This is the default setting
   */

  FIXED,

  /**
   * Cells are truncated when necessary
   */

  CLIPPING,

  /**
   * The table grows when necessary to accommodate additional columns/rows
   */

  GROW;
}