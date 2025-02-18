<template>
    <Text content="Um dich anzumelden, muss das folgende Feld ausgefüllt werden." :style="'regular'"  alignment="left"/>
    <TextField
        label="Nutzername"
        :required="true"
        placeholder="NeugierigerFuchs4850"
        v-model:content="username"
        :validator="CommonValidators.minLength(10)"
        v-model:validationSucceeded="validationSucceeded"
    />
</template>

<script setup lang="ts">
import {onMounted, ref, type Ref} from "vue";
import {type Router, useRouter} from "vue-router";
import {useStore} from "@/store.ts";
import TextField, { CommonValidators } from "@/components/TextField.vue";
import type {RouteEmit} from "@/App.vue";
import {Dialog} from "@/components/Dialog.ts";
import Text from "@/components/Text.vue";

const emit = defineEmits<RouteEmit>()

const router: Router = useRouter()
const store = useStore()

const validationSucceeded: Ref<boolean|undefined> = ref(undefined)
const username: Ref<string> = ref("")

onMounted(() => {
    emit(
        "route",
        {
            titles: ["Anmeldung"],
            buttonData: [
                {
                    text: "Zurück",
                    action: async () => {
                        await router.push({name: "start"})
                    },
                    type: "secondary"
                },
                {
                    text: "Anmelden",
                    action: async () => {
                        if(!validationSucceeded.value) return await Dialog.Common.validationFailed.show()

                        await store.loginUser(username.value)
                        await store.getSubjects()
                        await router.push({name: "subjects-overview"})
                    },
                    type: "primary"
                }
            ]
        }
    );
})
</script>