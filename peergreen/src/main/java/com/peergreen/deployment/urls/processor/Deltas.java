/**
 * Copyright 2013 Peergreen S.A.S. All rights reserved.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.peergreen.deployment.urls.processor;

import java.util.Iterator;
import java.util.List;

import com.peergreen.deployment.urls.processor.delta.Delta;

/**
 * @author Mohammed Boukada
 */
public class Deltas implements Iterable<Delta> {
    private final List<Delta> deltas;

    public Deltas(final List<Delta> deltas) {
        this.deltas = deltas;
    }

    @Override
    public Iterator<Delta> iterator() {
        return deltas.iterator();
    }
}
