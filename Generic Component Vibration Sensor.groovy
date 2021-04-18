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
0.1.13.1- 2021-04-18 @kkossev - test

*/

metadata
{
    definition(name: "Generic Component Vibration Sensor", namespace: "hubitat", author: "community", importUrl: "https://raw.githubusercontent.com/ymerj/HE-HA-control/main/genericComponentPressureSensor.groovy")
    {
		capability "Sensor"
        capability "Refresh"
   		capability "AccelerationSensor"    //acceleration - ENUM ["inactive", "active"]
		capability "MotionSensor"        // motion - ENUM ["inactive", "active"]
        capability "Battery"

    }
    preferences {
        input name: "txtEnable", type: "bool", title: "Enable descriptionText logging", defaultValue: true
        input(name: "resetTimeSetting", type: "number", title: "Reset Motion Timer", description: "After X number of seconds, reset motion to inactive (1 to 3600, default: 61)", defaultValue: "61", range: "1..3600")
    }
}

void updated() {
    log.info "Updated..."
    log.warn "description logging is: ${txtEnable == true}"
    state.LastActiveEvent = ""
}

void installed() {
    log.info "Installed..."
    device.updateSetting("txtEnable",[type:"bool",value:true])
    refresh()
}

void parse(String description) { log.warn "parse(String description) not implemented" }

void parse(List<Map> description) {
    log.info "!!!!!!!!!!!!!! Generic Component Vibration Sensor !!!!!!!!!!!!!!parse DESCRIPTION is... ${description} "
    description.each {
        if (it.name in ["action" ]) {
            switch (it.value) {
                case "vibration":
                case "tilt":
                case "drop":
                    it.name = it.value
                    it.value = "active"
                    it.descriptionText = this.device.displayName + " " + it.name + " is " + it.value
                    sendEvent(it)
                    //
                    it.name = "motion"
                    motion = "active"
                    it.type = "digital"
                    it.isStateChange = true 
                    it.descriptionText = this.device.displayName + " " + it.name + " is " + it.value
                    Integer resetTime = resetTimeSetting != null ? resetTimeSetting : 61
                    runIn(resetTime, "resetMotionEvent")
                    sendEvent(it)
                    break
                default :
                    break
            }
        }
        else if (it.name in ["battery"]) {
            it.descriptionText = it.descriptionText + it.name + " is " + it.value
            sendEvent(it)
        }
        //log.debug it.descriptionText
    }
}

void refresh() {
    parent?.componentRefresh(this.device)
}

void resetMotionEvent() {
    log.debug("resetMotionEvent()")
    sendEvent(name:"motion", value: "inactive", isStateChange: false, type: "digital", descriptionText: "$device.displayName motion was reset")
}





def active() {
	sendEvent("name": "motion", "value":  "active", isStateChange: true)
	if (txtEnable) log.info "$device.displayName motion changed to active [virtual]"
}

def inactive() {
	sendEvent("name": "motion", "value":  "inactive", isStateChange: true)
	if (txtEnable) log.info "$device.displayName motion changed to inactive [virtual]"
}

