<template>
    <Text content="Um dich zu registrieren, muss das folgende Feld ausgefüllt werden." :style="'regular'"  alignment="left"/>
    <TextField
        label="Registriercode"
        :required="true"
        placeholder="JDIWNC"
        v-model:content="registrationCode"
        :validator="CommonValidators.registrationCode"
        v-model:validationSucceeded="validationSucceeded"
    />
</template>

<script setup lang="ts">
import {onMounted, ref, type Ref} from "vue";
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

onMounted(() => {
    emit(
        "route",
        {
            titles: ["Registrierung", "Dateneingabe"],
            buttonData: [
                {
                    text: "Zurück",
                    action: async () => {
                        await router.push({name: "start"})
                    },
                    type: "secondary"
                },
                {
                    text: "Registrieren",
                    action: async () => {
                        if(!validationSucceeded.value) return Dialog.Common.validationFailed.show()

                        await store.registerUser(registrationCode.value)
                        await router.push({name: "registration_username"})
                    },
                    type: "primary"
                }
            ]
        }
    );
})
</script>