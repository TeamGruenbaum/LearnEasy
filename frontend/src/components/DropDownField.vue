<template>
    <div class="w-100">
        <label :id="labelId" class="form-label">{{ label + ":" }}</label>
        <select
            tabindex="0"
            :aria-labelledby="labelId"
            :aria-required="!noSelectionIsAllowed"
            :aria-invalid="!(isFirstSelection || noSelectionIsAllowed || selectedElementId !== undefined)"
            :aria-errormessage="errorId"
            @keydown.enter.space.prevent="showPicker"
            ref="select"
            :class="`form-select${!isFirstSelection ? (!noSelectionIsAllowed && selectedElementId === undefined ? ' is-invalid' : ' is-valid') : ''} `"
            v-model="selectedElementId"
            @change="isFirstSelection = false"
        >
            <option :value="undefined">Keine Auswahl getroffen</option>
            <template v-for="element in elements">
                <option :value="element.id">{{ element.name }}</option>
            </template>
        </select>
        <p
            :id="errorId"
            :aria-hidden="isFirstSelection || noSelectionIsAllowed ? 'true' : (selectedElementId === undefined ? 'false' : 'true')"
            class="mb-0"
            :class="`invalid-feedback d-block`"
            style="min-height: 30px"
        >
            {{ isFirstSelection || noSelectionIsAllowed ? "" : (selectedElementId === undefined ? "Es muss eine Auswahl getroffen werden." : "")}}
        </p>
    </div>
</template>

<script setup lang="ts">
import {ref, watch, type ModelRef, computed, useId} from "vue";

export type DropDownElement = { id: string, name: String }

defineOptions({inheritAttrs: false})
const props = withDefaults(
    defineProps<{ label: string, elements: Array<DropDownElement>, noSelectionIsAllowed?: boolean }>(),
    {noSelectionIsAllowed: false}
)

const selectedElementId: ModelRef<string | undefined> = defineModel("selectedElementId");
const isFirstSelection = ref(true);
const select = ref<HTMLSelectElement | null>(null);
const labelId = useId()
const errorId = useId()

function showPicker() {
    if (select.value) select.value.showPicker()
}

</script>