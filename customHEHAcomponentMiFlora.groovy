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

0.1.13- @tomw - initial template
0.1.13.1 -2021-04-10 - kkossev

*/

metadata
{
    definition(name: "Custom HEHA Component MiFlora", namespace: "hubitat", author: "community", importUrl: "https://raw.githubusercontent.com/ymerj/HE-HA-control/main/customHEHAcomponentMiFlora.groovy")
    {
       capability "pHMeasurement"
       capability "IlluminanceMeasurement"
       capability "RelativeHumidityMeasurement"
       capability "TemperatureMeasurement"

        capability "Sensor"
        capability "Refresh"

        attribute "originalEntity", "String"
    }
    preferences {
        input name: "txtEnable", type: "bool", title: "Enable descriptionText logging", defaultValue: true
    }
}

void updated() {
    log.info "Updated...Generic Component MiFlora"
    log.warn "description logging is: ${txtEnable == true}"
    state.LastActiveEvent = ""
}

void installed() {
    log.info "Installed...Generic Component MiFlora"
    device.updateSetting("txtEnable",[type:"bool",value:true])
    refresh()
}

void parse(String description) { log.warn "parse(String description) not implemented" }

void parse(List<Map> description) {
    description.each {
        if (it.name in ["conductivity" ]) {
            it.name = "pH"
            it.descriptionText = it.descriptionText + it.name + " is " + it.value 
            sendEvent(it)
        }
        else if (it.name in ["illuminance"]) {
            it.descriptionText = it.descriptionText + it.name + " is " + it.value
            sendEvent(it)
        }
        else if (it.name in ["Moisture", "moisture"]) {
            it.name = "humidity"
            it.descriptionText = it.descriptionText + it.name + " is " + it.value 
            sendEvent(it)
        }
        else if (it.name in ["temperature"]) {
            it.descriptionText = it.descriptionText + it.name + " is " + it.value 
            sendEvent(it)
        }
        log.debug it.descriptionText
    }
}

void refresh() {
    parent?.componentRefresh(this.device)
}

