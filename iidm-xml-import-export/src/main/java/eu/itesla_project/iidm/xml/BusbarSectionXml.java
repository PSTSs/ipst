/**
 * Copyright (c) 2016, RTE (http://www.rte-france.com)
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package eu.itesla_project.iidm.xml;

import eu.itesla_project.iidm.network.Bus;
import eu.itesla_project.iidm.network.BusbarSection;
import eu.itesla_project.iidm.network.BusbarSectionAdder;
import eu.itesla_project.iidm.network.VoltageLevel;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.util.List;

/**
 *
 * @author Geoffroy Jamgotchian <geoffroy.jamgotchian at rte-france.com>
 */
class BusbarSectionXml extends IdentifiableXml<BusbarSection, BusbarSectionAdder, VoltageLevel> {

    static final BusbarSectionXml INSTANCE = new BusbarSectionXml();

    static final String ROOT_ELEMENT_NAME = "busbarSection";

    @Override
    protected String getRootElementName() {
        return ROOT_ELEMENT_NAME;
    }

    @Override
    protected boolean hasSubElements(BusbarSection bs) {
        return false;
    }

    @Override
    protected void writeRootElementAttributes(BusbarSection bs, VoltageLevel vl, XmlWriterContext context) throws XMLStreamException {
        XmlUtil.writeInt("node", bs.getTerminal().getNodeBreakerView().getNode(), context.getWriter());
        XmlUtil.writeFloat("v", bs.getV(), context.getWriter());
        XmlUtil.writeFloat("angle", bs.getAngle(), context.getWriter());
    }

    @Override
    protected void writeSubElements(BusbarSection bs, VoltageLevel vl, XmlWriterContext context) throws XMLStreamException {
    }

    @Override
    protected BusbarSectionAdder createAdder(VoltageLevel vl) {
        return vl.getNodeBreakerView().newBusbarSection();
    }

    @Override
    protected BusbarSection readRootElementAttributes(BusbarSectionAdder adder, XMLStreamReader reader, List<Runnable> endTasks) {
        int node = XmlUtil.readIntAttribute(reader, "node");
        float v = XmlUtil.readOptionalFloatAttribute(reader, "v");
        float angle = XmlUtil.readOptionalFloatAttribute(reader, "angle");
        BusbarSection bbs = adder.setNode(node)
                .add();
        endTasks.add(() -> {
            Bus b = bbs.getTerminal().getBusView().getBus();
            if (b != null) {
                b.setV(v).setAngle(angle);
            }
        });
        return bbs;
    }

    @Override
    protected void readSubElements(BusbarSection bs, XMLStreamReader reader, List<Runnable> endTasks) throws XMLStreamException {
        readUntilEndRootElement(reader, () -> BusbarSectionXml.super.readSubElements(bs, reader,endTasks));
    }
}
