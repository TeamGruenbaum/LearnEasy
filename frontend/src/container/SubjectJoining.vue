<template>
    <Text content="Um einem Fach beizutreten, muss das folgende Feld ausgefüllt werden." :style="'regular'"  alignment="left"/>
    <TextField
        label="Registriercode"
        :required="true"
        placeholder="JDIWNC"
        v-model:content="registrationCode"
        :validator="CommonValidators.registrationCode"
        v-model:validationSucceeded="validationSucceeded"
    />
</template>

<script lang="ts" setup>
import {onMounted, type Ref, ref} from "vue";
import {type Router, useRouter} from "vue-router";
import TextField, { CommonValidators } from "@/components/TextField.vue";
import {useStore} from "@/store.ts";
import type {RouteEmit} from "@/App.vue";
import {Dialog} from "@/components/Dialog.ts";
import Text from "@/components/Text.vue";

const emit = defineEmits<RouteEmit>()

const store = useStore()
const router: Router = useRouter()

const validationSucceeded: Ref<boolean|undefined> = ref()
const registrationCode: Ref<string> = ref("")

onMounted(async () => {
    emit(
        "route",
        {
            titles: ["Fachbeitritt"],
            buttonData: [
                {
                    text: "Abbrechen",
                    action: async () => { await router.push({name: "subjects-overview"}) },
                    type: "secondary"
                },
                {
                    text: "Beitreten",
                    action: async () => {
                        if(!validationSucceeded.value) return await Dialog.Common.validationFailed.show()

                        await store.joinSubject(registrationCode.value)
                        await router.push({name: "subjects-overview"})
                    },
                    type: "primary"
                }
            ]
        }
    );
})
</script>