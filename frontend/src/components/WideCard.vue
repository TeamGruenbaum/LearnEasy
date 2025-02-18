<template>
    <div
        :tabindex="onClick === undefined ? undefined : 0"
        :role="onClick === undefined ? 'group' : 'button'"
        :aria-labelledby="labelId"
        @keydown.enter.space.prevent="onClick ? onClick() : undefined"
        class="card w-100 focus-ring"
        @click="onClick ? onClick() : undefined"
        style="min-height: 70px"
    >
        <div :id="labelId" class="card-body text-center d-flex align-items-center mx-auto">
            <div>
                <Text v-for="(content, index) in contents" :key="index" :style="'regular'" :content="content" :alignment="'center'"/>
            </div>
            <img v-if="showSticker" src="/stamp.png" alt="Stempel mit Fuchs" style="position: absolute; right: 1.5%; top: 50%; transform: translateY(-50%); height: 50px;">
        </div>
    </div>
</template>


<script setup lang="ts">
import Text from "@/components/Text.vue";
import {useId} from "vue";

defineOptions({inheritAttrs: false})
const props = defineProps<{
    contents: Array<string>,
    showSticker: Boolean,
    onClick: Function | undefined
}>()

const labelId = useId()
</script>