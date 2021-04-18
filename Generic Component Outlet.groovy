/*

Copyright 2021

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

-------------------------------------------

Change history:

0.1.13- @tomw - initial version
0.1.13.1 2021-04-18 - @kkossev - test

*/

metadata
{
    definition(name: "Generic Component Outlet", namespace: "hubitat", author: "community", importUrl: "https://raw.githubusercontent.com/ymerj/HE-HA-control/main/genericComponentPressureSensor.groovy")
    {
        capability "Outlet"        //Attributes switch - ENUM ["on", "off"] Commands off() on()
        capability "EnergyMeter"   //Attributes energy - NUMBER, unit:kWh
        capability "PowerMeter"    // Attributes power - NUMBER, unit:W
        capability "Actuator"
		capability "Sensor"
        capability "Refresh"

        attribute "originalEntity", "String" 
    }
    preferences {
        input name: "txtEnable", type: "bool", title: "Enable descriptionText logging", defaultValue: true
    }
}

void updated() {
    log.info "Updated...Generic Component Outlet"
    log.warn "description logging is: ${txtEnable == true}"
    state.LastActiveEvent = ""
}

void installed() {
    log.info "Installed...Generic Component Outlet"
    device.updateSetting("txtEnable",[type:"bool",value:true])
    refresh()
}

void parse(String description) { log.warn "parse(String description) not implemented" }

void parse(List<Map> description) {
    log.info "!!!!!!!!!!!!!! Generic Component Outlet !!!!!!!!!!!!!!parse DESCRIPTION is... ${description} "
    
    description.each {
        if (it.name in ["consumption" ]) {
            it.name = "energy"
            it.descriptionText = it.descriptionText + it.name + " is " + it.value + " kWh"
            sendEvent(it)
        }
        else if (it.name in ["action" ]) {
            it.name = "switch"
            it.descriptionText = it.descriptionText + it.name + " is " + it.value
            sendEvent(it)
        }
        else if (it.name in ["power" ]) {
            it.name = "power"
            it.descriptionText = it.descriptionText + it.name + " is " + it.value + " W"
            sendEvent(it)
        }
        log.debug it.descriptionText
    }
    
    
}

void refresh() {
    parent?.componentRefresh(this.device)
}


void on() {    
   log.debug("on()")
   parent?.customComponentOn(this.device)
}

void off() {    
   log.debug("off()")
   parent?.customComponentOff(this.device)
}

