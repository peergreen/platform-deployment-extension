/**
 * Copyright 2013 Peergreen S.A.S. All rights reserved.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.peergreen.deployment.urls.processor.delta;

import com.peergreen.deployment.Artifact;

/**
 * @author Mohammed Boukada
 */
public class Delta {
    private final Artifact previous;
    private final Artifact actual;
    private final Kind kind;

    public Delta(Artifact previous, Artifact actual, Kind kind) {
        this.previous = previous;
        this.actual = actual;
        this.kind = kind;
    }

    public Artifact getPrevious() {
        return previous;
    }

    public Artifact getActual() {
        return actual;
    }

    public Kind getKind() {
        return kind;
    }
}
