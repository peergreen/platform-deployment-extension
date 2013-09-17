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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import org.apache.felix.ipojo.annotations.Requires;

import com.peergreen.deployment.ProcessorContext;
import com.peergreen.deployment.ProcessorException;
import com.peergreen.deployment.processor.Phase;
import com.peergreen.deployment.processor.Processor;
import com.peergreen.deployment.urls.UrlsDeploymentPlan;
import com.peergreen.deployment.urls.processor.delta.Delta;
import com.peergreen.deployment.urls.processor.delta.UrlsDeploymentPlanDeltaService;
import com.peergreen.deployment.urls.processor.parser.UrlsDeploymentPlanParser;

/**
 * @author Mohammed Boukada
 */
@Processor
@Phase("urls-compute-diff")
public class UrlsDeltaProcessor {

    private UrlsDeploymentPlanParser parser;
    private UrlsDeploymentPlanDeltaService deltaService;

    public UrlsDeltaProcessor(@Requires UrlsDeploymentPlanParser parser,
                              @Requires UrlsDeploymentPlanDeltaService deltaService) {
        this.parser = parser;
        this.deltaService = deltaService;
    }

    public void handle(UrlsDeploymentPlan previous, ProcessorContext processorContext) throws ProcessorException {
        try {
            URL url = processorContext.getArtifact().uri().toURL();
            UrlsDeploymentPlan actual = parser.parse(url.openStream());
            List<Delta> deltas = deltaService.delta(previous, actual);
            processorContext.addFacet(Deltas.class, new Deltas(deltas));
        } catch (MalformedURLException e) {
            throw new ProcessorException("Unable to get URL", e);
        } catch (IOException e) {
            throw new ProcessorException("Unable to read the file", e);
        } catch (URISyntaxException e) {
            throw new ProcessorException("Unable to build URI", e);
        }
    }
}
