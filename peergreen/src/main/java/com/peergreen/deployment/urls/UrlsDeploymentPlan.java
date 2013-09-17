/**
 * Copyright 2013 Peergreen S.A.S. All rights reserved.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.peergreen.deployment.urls;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.peergreen.deployment.Artifact;

/**
 * @author Mohammed Boukada
 */
public class UrlsDeploymentPlan {

    private final List<Artifact> artifacts = new ArrayList<>();

    public void add(Artifact artifact) {
        artifacts.add(artifact);
    }

    public void remove(URI uri) {
        Artifact artifact = getArtifact(uri);
        artifacts.remove(artifact);
    }

    private Artifact getArtifact(URI uri) {
        for (Artifact artifact : artifacts) {
            if (uri.equals(artifact.uri())) {
                return artifact;
            }
        }
        return null;
    }

    public Collection<Artifact> getArtifacts() {
        return artifacts;
    }
}
