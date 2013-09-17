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

import static com.peergreen.deployment.urls.processor.delta.Kind.REMOVED;
import static com.peergreen.deployment.urls.processor.delta.Kind.UNCHANGED;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;

import com.peergreen.deployment.Artifact;
import com.peergreen.deployment.urls.UrlsDeploymentPlan;

/**
 * @author Mohammed Boukada
 */
@Component
@Provides
@Instantiate
public class BaseUrlsDeploymentPlanDeltaService implements UrlsDeploymentPlanDeltaService {

    @Override
    public List<Delta> delta(UrlsDeploymentPlan previous, UrlsDeploymentPlan actual) {
        Deque<Artifact> previousUrls = new ArrayDeque<>(previous.getArtifacts());
        Deque<Artifact> actualUrls = new ArrayDeque<>(actual.getArtifacts());
        return computeDelta(previousUrls, actualUrls);
    }

    private List<Delta> computeDelta(Deque<Artifact> previous, Deque<Artifact> actual) {
        List<Delta> deltas = new ArrayList<>();
        while (!previous.isEmpty()) {
            Artifact old = previous.pollFirst();
            Artifact match = findUrlDeployableInfo(old, actual);
            if (match != null) {
                // no change
                deltas.add(new Delta(old, match, UNCHANGED));
            } else {
                // url was removed
                deltas.add(new Delta(old, null, REMOVED));
            }
        }

        // Remaining items in actual are new Items
        while (!actual.isEmpty()) {
            deltas.add(new Delta(null, actual.pop(), Kind.ADDED));
        }
        return deltas;
    }

    private Artifact findUrlDeployableInfo(Artifact old, Deque<Artifact> actual) {
        Deque<Artifact> copy = new ArrayDeque<>(actual);
        for (Artifact artifact : copy) {
            if (old.uri().equals(artifact.uri())) {
                actual.remove(artifact);
                return artifact;
            }
        }
        return null;
    }
}
