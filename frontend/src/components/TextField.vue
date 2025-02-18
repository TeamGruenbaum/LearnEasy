<template>
    <div class="w-100">
        <label :id="labelId" class="form-label">{{ label + " " + (required ? "(Erforderlich)" : "(Optional)") + ":" }}</label>
        <input
            tabindex="0"
            :aria-labelledby="labelId"
            :placeholder="props.placeholder"
            :aria-required="props.required"
            :aria-invalid="!(validationSucceeded ?? true)"
            :aria-errormessage="errorId"
            type="text"
            :class="`form-control${typeof validationCssClass === 'undefined' ? '' : ' '+validationCssClass}`"
            v-model="content"
            :readonly="readonly"
        >
        <p
            :id="errorId"
            :aria-hidden="validationSucceeded || validationSucceeded === undefined ? 'true' : 'false'"
            class="mb-0"
            :class="`invalid-feedback d-block`"
            style="min-height: 30px"
        >
            {{ validationSucceeded || validationSucceeded === undefined ? "" : validationMessage}}
        </p>
    </div>
</template>

<script setup lang="ts">
import {type ModelRef, ref, type Ref, useId, watch} from "vue";

export type TextFieldValidationResult = { succeeded: boolean | undefined, failedMessage?: string }

defineOptions({inheritAttrs: false})
const props = withDefaults(
    defineProps<{ label: string, required: boolean, readonly?: boolean, placeholder?: string, validationActive?: boolean, validator?: (value: string) => TextFieldValidationResult }>(),
    {label: "", readonly: false, placeholder: undefined, validationActive: true, validator: undefined}
)
const content: ModelRef<string> = defineModel("content", {default: ""})
const validationSucceeded: ModelRef<boolean|undefined> = defineModel("validationSucceeded", { default: undefined as boolean|undefined, required: false })

const validationMessage: Ref<string> = ref("")
const validationCssClass: Ref<string> = ref("")

const labelId = useId()
const errorId = useId()

function checkValidation() {
    if (props.validationActive && props.validator) {
        const validationResult: TextFieldValidationResult = props.validator(content.value)

        validationSucceeded.value = validationResult.succeeded
        validationMessage.value = validationResult.failedMessage ?? ""
    }
}
watch(() => props.validationActive, (newValue, oldValue) => {
    if(newValue) checkValidation()
    else {
        validationSucceeded.value = undefined
        validationMessage.value = ""
        validationCssClass.value = ""
    }
})
watch(content, () => checkValidation());
watch(validationSucceeded, () => {
    if (validationSucceeded.value === undefined) validationCssClass.value = ""
    else if (validationSucceeded.value) validationCssClass.value = "is-valid"
    else validationCssClass.value = "is-invalid"
});
</script>

<script lang="ts">
export const CommonValidators = {
    minLength: (minLength: number) => (value: string): TextFieldValidationResult => {
        if (value.length < minLength) return {succeeded: false, failedMessage: `Das Textfeld muss mindestens ${minLength} Zeichen beinhalten.`}
        return {succeeded: true}
    },
    registrationCode: (value: string): TextFieldValidationResult => {
        if (value.length === 6 && value.match(/^[A-Z]*$/)) return {succeeded: true}
        return {succeeded: false, failedMessage: "Der Textfeld muss aus 6 Großbuchstaben beinhalten."}
    },
};
</script>