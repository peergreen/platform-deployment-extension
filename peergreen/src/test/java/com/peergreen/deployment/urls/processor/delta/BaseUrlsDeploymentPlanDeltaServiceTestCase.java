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

import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.peergreen.deployment.Artifact;
import com.peergreen.deployment.urls.UrlsDeploymentPlan;

/**
 * @author Mohammed Boukada
 */
public class BaseUrlsDeploymentPlanDeltaServiceTestCase {

    private UrlsDeploymentPlanDeltaService deltaService;
    private UrlsDeploymentPlan previous;
    private UrlsDeploymentPlan actual;

    private String helloName;
    private URI helloURI;

    @Mock
    private Artifact helloArtifact;

    @BeforeMethod
    public void init() throws URISyntaxException {
        helloName = "hello";
        helloURI = new URI("mvn:com.peergreen.example.paxexam/paxexam-hello-service/1.0.0");
        deltaService = new BaseUrlsDeploymentPlanDeltaService();
        previous = new UrlsDeploymentPlan();
        actual = new UrlsDeploymentPlan();

        MockitoAnnotations.initMocks(this);
        when(helloArtifact.name()).thenReturn(helloName);
        when(helloArtifact.uri()).thenReturn(helloURI);
    }

    @Test
    public void testUrlsDeploymentPlanUnchanged() throws URISyntaxException {
        previous.add(helloArtifact);
        actual.add(helloArtifact);

        List<Delta> deltas = deltaService.delta(previous, actual);
        assertEquals(deltas.size(), 1);

        Delta delta = deltas.get(0);
        assertEquals(delta.getActual().name(), helloName);
        assertEquals(delta.getActual().uri(), helloURI);
        assertEquals(delta.getPrevious().name(), helloName);
        assertEquals(delta.getPrevious().uri(), helloURI);
        assertEquals(delta.getKind(), Kind.UNCHANGED);
    }

    @Test
    public void testUrlsDeploymentPlanAdded() throws URISyntaxException {
        actual.add(helloArtifact);

        List<Delta> deltas = deltaService.delta(previous, actual);
        assertEquals(deltas.size(), 1);

        Delta delta = deltas.get(0);
        assertEquals(delta.getActual().name(), helloName);
        assertEquals(delta.getActual().uri(), helloURI);
        assertNull(delta.getPrevious());
        assertEquals(delta.getKind(), Kind.ADDED);
    }

    @Test
    public void testUrlsDeploymentPlanRemoved() throws URISyntaxException {
        previous.add(helloArtifact);

        List<Delta> deltas = deltaService.delta(previous, actual);
        assertEquals(deltas.size(), 1);

        Delta delta = deltas.get(0);
        assertNull(delta.getActual());
        assertEquals(delta.getPrevious().name(), helloName);
        assertEquals(delta.getPrevious().uri(), helloURI);
        assertEquals(delta.getKind(), Kind.REMOVED);
    }
}
